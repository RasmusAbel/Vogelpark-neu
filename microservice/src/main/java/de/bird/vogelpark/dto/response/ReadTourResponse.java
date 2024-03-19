package de.bird.vogelpark.dto.response;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public record ReadTourResponse(
        String name,
        String description,
        int price,
        String startTime,
        String endTime,
        String duration,
        String imageUrl,
        Set<String> attractionNames
) {
    public ReadTourResponse {
        if (attractionNames == null) {
            attractionNames = new HashSet<>();
        }
    }
}
