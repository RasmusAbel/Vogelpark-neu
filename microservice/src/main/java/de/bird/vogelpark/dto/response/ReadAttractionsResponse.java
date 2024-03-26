package de.bird.vogelpark.dto.response;

import java.util.List;

public record ReadAttractionsResponse(
        String name,
        String description,

        String imageUrl,
        List<OpeningHoursResponse> openingHoursResponses,
        List<String> filterTagResponses,
        List<String> tourNames
) { }
