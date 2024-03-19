package de.bird.vogelpark.dto.response;

import java.util.ArrayList;
import java.util.List;

/*
public class ReadAttractionsResponse {
    private String name;
    private String description;
    private List<OpeningHoursResponse> openingHoursResponses = new ArrayList<>();
    private List<String> filterTagResponses = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OpeningHoursResponse> getOpeningHoursResponses() {
        return openingHoursResponses;
    }

    public List<String> getFilterTagResponses() {
        return filterTagResponses;
    }
}
 */

public record ReadAttractionsResponse(
        String name,
        String description,

        String imageUrl,
        List<OpeningHoursResponse> openingHoursResponses,
        List<String> filterTagResponses
) { }
