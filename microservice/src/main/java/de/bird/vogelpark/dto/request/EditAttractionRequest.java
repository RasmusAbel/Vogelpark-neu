package de.bird.vogelpark.dto.request;

/*
public class EditAttractionRequest {
    private String currentName;
    private String newName;
    private String newDescription;
    private CreateOpeningHoursRequest[] openingHoursToAdd;
    private Long[] openingHourIdsToRemove;
    private String[] filterTagsToAdd;
    private String[] filterTagsToRemove;

    public String getCurrentName() {
        return currentName;
    }

    public String getNewName() {
        return newName;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public CreateOpeningHoursRequest[] getOpeningHoursToAdd() {
        return openingHoursToAdd;
    }

    public Long[] getOpeningHourIdsToRemove() {
        return openingHourIdsToRemove;
    }

    public String[] getFilterTagsToAdd() {
        return filterTagsToAdd;
    }

    public String[] getFilterTagsToRemove() {
        return filterTagsToRemove;
    }
}
 */

public record EditAttractionRequest(
        String currentName,
        String newName,
        String newDescription,
        String newImageUrl,
        CreateOpeningHoursRequest[] openingHoursToAdd,
        Long[] openingHourIdsToRemove,
        String[] filterTagsToAdd,
        String[] filterTagsToRemove
) {
    public EditAttractionRequest {
        if (openingHoursToAdd == null) {
            openingHoursToAdd = new CreateOpeningHoursRequest[0];
        }
        if (openingHourIdsToRemove == null) {
            openingHourIdsToRemove = new Long[0];
        }
        if (filterTagsToAdd == null) {
            filterTagsToAdd = new String[0];
        }
        if (filterTagsToRemove == null) {
            filterTagsToRemove = new String[0];
        }
    }
}
