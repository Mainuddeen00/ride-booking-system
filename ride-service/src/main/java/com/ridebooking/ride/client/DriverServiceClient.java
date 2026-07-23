package com.ridebooking.ride.client;

import com.ridebooking.ride.dto.DriverDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverServiceClient {

    private final RestTemplate restTemplate;

    public List<DriverDto> getAvailableDrivers() {
        try {
            log.info("Calling DRIVER-SERVICE to get available drivers...");
            
            DriverDto[] drivers = restTemplate.getForObject(
                    "http://DRIVER-SERVICE/api/v1/drivers",
                    DriverDto[].class
            );
            
            if (drivers == null) {
                log.info("No drivers returned from driver service");
                return List.of();
            }
            
            log.info("Found {} total drivers", drivers.length);
            
            // Filter for ONLINE drivers
            List<DriverDto> onlineDrivers = Arrays.stream(drivers)
                    .filter(driver -> "ONLINE".equalsIgnoreCase(driver.getAvailabilityStatus()))
                    .collect(Collectors.toList());
            
            log.info("Found {} ONLINE drivers", onlineDrivers.size());
            return onlineDrivers;
            
        } catch (Exception e) {
            log.error("Error calling driver service: {}", e.getMessage());
            return List.of();
        }
    }
}
