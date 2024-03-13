package de.bird.vogelpark.dto.request;

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
