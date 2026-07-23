package com.ridebooking.ride.controller;

import com.ridebooking.ride.dto.RideRequestDto;
import com.ridebooking.ride.dto.RideResponseDto;
import com.ridebooking.ride.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/request")
    public ResponseEntity<RideResponseDto> requestRide(@Valid @RequestBody RideRequestDto request) {
        return ResponseEntity.ok(rideService.requestRide(request));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<RideResponseDto> acceptRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.acceptRide(id));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<RideResponseDto> startRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.startRide(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<RideResponseDto> completeRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.completeRide(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<RideResponseDto> cancelRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.cancelRide(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDto> getRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRide(id));
    }

    @GetMapping("/rider/{riderId}")
    public ResponseEntity<List<RideResponseDto>> getRidesByRider(@PathVariable Long riderId) {
        return ResponseEntity.ok(rideService.getRidesByRider(riderId));
    }
}
