package de.bird.vogelpark.dto.request;

public class CreateAttractionRequest {
    private String name;
    private String description;
    private CreateOpeningHoursRequest[] openingHours;
    private String[] filterTags;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CreateOpeningHoursRequest[] getOpeningHours() {
        return openingHours;
    }

    public String[] getFilterTags() {
        return filterTags;
    }
}
