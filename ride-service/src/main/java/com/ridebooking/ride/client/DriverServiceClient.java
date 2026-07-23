package com.ridebooking.ride.client;

import com.ridebooking.ride.dto.DriverDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DriverServiceClient {

    private final RestTemplate restTemplate;

    public List<DriverDto> getAvailableDrivers() {
        try {
            DriverDto[] drivers = restTemplate.getForObject(
                    "http://driver-service/api/drivers/available",
                    DriverDto[].class
            );
            return drivers != null ? List.of(drivers) : List.of();
        } catch (Exception e) {
            // driver-service is unreachable (not registered with Eureka, down, network issue, etc.)
            // Treat this the same as "no drivers found" rather than crashing the whole request.
            return List.of();
        }
    }
}