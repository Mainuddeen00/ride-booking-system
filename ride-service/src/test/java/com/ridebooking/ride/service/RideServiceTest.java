package com.ridebooking.ride.service;

import com.ridebooking.ride.client.DriverServiceClient;
import com.ridebooking.ride.dto.DriverDto;
import com.ridebooking.ride.dto.RideRequestDto;
import com.ridebooking.ride.dto.RideResponseDto;
import com.ridebooking.ride.entity.Ride;
import com.ridebooking.ride.entity.RideStatus;
import com.ridebooking.ride.exception.NoDriverAvailableException;
import com.ridebooking.ride.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverServiceClient driverServiceClient;

    @InjectMocks
    private RideService rideService;

    // tests go here, next step
    @Test
    void requestRide_shouldAssignNearestDriver() {
        // ---- Arrange: set up the fake data our mocks will return ----
        RideRequestDto request = new RideRequestDto();
        request.setRiderId(1L);
        request.setPickupLocation("MG Road");
        request.setDropLocation("Whitefield");
        request.setPickupLatitude(12.9758);
        request.setPickupLongitude(77.6045);
        request.setDropLatitude(12.9698);
        request.setDropLongitude(77.7500);

        DriverDto nearDriver = new DriverDto();
        nearDriver.setId(101L);
        nearDriver.setCurrentLatitude(12.9760); // very close to pickup
        nearDriver.setCurrentLongitude(77.6050);

        DriverDto farDriver = new DriverDto();
        farDriver.setId(202L);
        farDriver.setCurrentLatitude(13.5000); // far away
        farDriver.setCurrentLongitude(78.0000);

        when(driverServiceClient.getAvailableDrivers())
                .thenReturn(List.of(farDriver, nearDriver));

        // Simulate the repository "saving" a ride by just returning what it was given,
        // but pretend it now has a generated ID (like a real DB would assign)
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> {
            Ride ride = invocation.getArgument(0);
            ride.setId(1L);
            return ride;
        });

        // ---- Act: call the real method we're testing ----
        RideResponseDto response = rideService.requestRide(request);

        // ---- Assert: verify the outcome is correct ----
        assertThat(response.getDriverId()).isEqualTo(101L); // the NEAR driver, not the far one
        assertThat(response.getStatus()).isEqualTo(RideStatus.DRIVER_ASSIGNED);
        assertThat(response.getRiderId()).isEqualTo(1L);
    }
    @Test
    void requestRide_shouldThrowException_whenNoDriversAvailable() {
        // ---- Arrange ----
        RideRequestDto request = new RideRequestDto();
        request.setRiderId(1L);
        request.setPickupLocation("MG Road");
        request.setDropLocation("Whitefield");
        request.setPickupLatitude(12.9758);
        request.setPickupLongitude(77.6045);
        request.setDropLatitude(12.9698);
        request.setDropLongitude(77.7500);

        when(driverServiceClient.getAvailableDrivers())
                .thenReturn(List.of()); // empty list = no drivers

        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> {
            Ride ride = invocation.getArgument(0);
            ride.setId(1L);
            return ride;
        });

        // ---- Act + Assert combined ----
        assertThatThrownBy(() -> rideService.requestRide(request))
                .isInstanceOf(NoDriverAvailableException.class)
                .hasMessage("No drivers currently available");
    }
    @Test
    void acceptRide_shouldThrowException_whenRideNotInDriverAssignedState() {
        // ---- Arrange: a ride that's already ACCEPTED, not DRIVER_ASSIGNED ----
        Ride existingRide = Ride.builder()
                .id(5L)
                .riderId(1L)
                .status(RideStatus.ACCEPTED)
                .build();

        when(rideRepository.findById(5L)).thenReturn(Optional.of(existingRide));

        // ---- Act + Assert ----
        assertThatThrownBy(() -> rideService.acceptRide(5L))
                .isInstanceOf(com.ridebooking.ride.exception.InvalidRideStateException.class)
                .hasMessageContaining("Ride must be DRIVER_ASSIGNED to be accepted");
    }
}