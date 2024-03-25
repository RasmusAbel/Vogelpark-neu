package de.bird.vogelpark.dto.request;

public record CreateTourRequest(String name,

                                String description,

                                int priceCents,

                                String imageUrl,

                                Integer startTimeHour, Integer startTimeMinute,
                                Integer endTimeHour, Integer endTimeMinute,
                                String[] attractionNames) {
}
