package com.ridebooking.ride.service;

import com.ridebooking.ride.client.DriverServiceClient;
import com.ridebooking.ride.dto.DriverDto;
import com.ridebooking.ride.dto.RideRequestDto;
import com.ridebooking.ride.dto.RideResponseDto;
import com.ridebooking.ride.entity.Ride;
import com.ridebooking.ride.entity.RideStatus;
import com.ridebooking.ride.exception.NoDriverAvailableException;
import com.ridebooking.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ridebooking.ride.exception.RideNotFoundException;
import com.ridebooking.ride.exception.InvalidRideStateException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final DriverServiceClient driverServiceClient;
    private static final double BASE_FARE = 30.0;
    private static final double RATE_PER_KM = 12.0;

    // ---- Ride booking + driver matching ----
    public RideResponseDto requestRide(RideRequestDto request) {
        Ride ride = Ride.builder()
                .riderId(request.getRiderId())
                .pickupLocation(request.getPickupLocation())
                .dropLocation(request.getDropLocation())
                .pickupLatitude(request.getPickupLatitude())
                .pickupLongitude(request.getPickupLongitude())
                .dropLatitude(request.getDropLatitude())
                .dropLongitude(request.getDropLongitude())
                .status(RideStatus.REQUESTED)
                .requestedAt(LocalDateTime.now())
                .build();

        ride = rideRepository.save(ride);

        List<DriverDto> availableDrivers = driverServiceClient.getAvailableDrivers();
        DriverDto nearestDriver = findNearestDriver(availableDrivers, request.getPickupLatitude(), request.getPickupLongitude());

        if (nearestDriver == null) {
            throw new NoDriverAvailableException();
        }

        ride.setDriverId(nearestDriver.getId());
        ride.setStatus(RideStatus.DRIVER_ASSIGNED);
        ride = rideRepository.save(ride);

        return toDto(ride);
    }

    // ---- Trip lifecycle transitions ----
    public RideResponseDto acceptRide(Long rideId) {
        Ride ride = getRideOrThrow(rideId);
        requireStatus(ride, RideStatus.DRIVER_ASSIGNED, "Ride must be DRIVER_ASSIGNED to be accepted");
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());
        return toDto(rideRepository.save(ride));
    }

    public RideResponseDto startRide(Long rideId) {
        Ride ride = getRideOrThrow(rideId);
        requireStatus(ride, RideStatus.ACCEPTED, "Ride must be ACCEPTED to start");
        ride.setStatus(RideStatus.ONGOING);
        ride.setStartedAt(LocalDateTime.now());
        return toDto(rideRepository.save(ride));
    }

    public RideResponseDto completeRide(Long rideId) {
        Ride ride = getRideOrThrow(rideId);
        requireStatus(ride, RideStatus.ONGOING, "Ride must be ONGOING to complete");

        double distanceKm = calculateDistanceKm(
                ride.getPickupLatitude(), ride.getPickupLongitude(),
                ride.getDropLatitude(), ride.getDropLongitude());

        double fare = BASE_FARE + (distanceKm * RATE_PER_KM);

        ride.setFare(Math.round(fare * 100.0) / 100.0);
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());
        return toDto(rideRepository.save(ride));
    }

    public RideResponseDto cancelRide(Long rideId) {
        Ride ride = getRideOrThrow(rideId);
        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidRideStateException("Cannot cancel a ride that is already " + ride.getStatus());
        }
        ride.setStatus(RideStatus.CANCELLED);
        return toDto(rideRepository.save(ride));
    }

    // ---- Read operations ----
    public RideResponseDto getRide(Long rideId) {
        return toDto(getRideOrThrow(rideId));
    }

    public List<RideResponseDto> getRidesByRider(Long riderId) {
        return rideRepository.findByRiderId(riderId).stream().map(this::toDto).toList();
    }

    // ---- Helpers ----
    private Ride getRideOrThrow(Long id) {
        return rideRepository.findById(id).orElseThrow(() -> new RideNotFoundException(id));
    }

    private void requireStatus(Ride ride, RideStatus expected, String message) {
        if (ride.getStatus() != expected) {
            throw new InvalidRideStateException(message + " (current status: " + ride.getStatus() + ")");
        }
    }

    private DriverDto findNearestDriver(List<DriverDto> drivers, double lat, double lng) {
        return drivers.stream()
                .min(Comparator.comparingDouble(d ->
                        calculateDistanceKm(lat, lng, d.getCurrentLatitude(), d.getCurrentLongitude())))
                .orElse(null);
    }

    private double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private RideResponseDto toDto(Ride ride) {
        return RideResponseDto.builder()
                .id(ride.getId())
                .riderId(ride.getRiderId())
                .driverId(ride.getDriverId())
                .pickupLocation(ride.getPickupLocation())
                .dropLocation(ride.getDropLocation())
                .status(ride.getStatus())
                .fare(ride.getFare())
                .requestedAt(ride.getRequestedAt())
                .completedAt(ride.getCompletedAt())
                .build();
    }
}