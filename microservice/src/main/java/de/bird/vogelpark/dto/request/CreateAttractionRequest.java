package de.bird.vogelpark.dto.request;

/*
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
*/

public record CreateAttractionRequest(
        String name,
        String description,
        CreateOpeningHoursRequest[] openingHours,
        String[] filterTags
) {
    public CreateAttractionRequest {
        if(openingHours == null) {
            openingHours = new CreateOpeningHoursRequest[0];
        }
        if(filterTags == null) {
            filterTags = new String[0];
        }
    }
}

