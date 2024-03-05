package de.bird.vogelpark.dto.response;

import java.util.ArrayList;
import java.util.List;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OpeningHoursResponse> getOpeningHoursResponses() {
        return openingHoursResponses;
    }

    public void setOpeningHoursResponses(List<OpeningHoursResponse> openingHoursResponses) {
        this.openingHoursResponses = openingHoursResponses;
    }

    public List<String> getFilterTagResponses() {
        return filterTagResponses;
    }

    public void setFilterTagResponses(List<String> filterTagResponses) {
        this.filterTagResponses = filterTagResponses;
    }
}
