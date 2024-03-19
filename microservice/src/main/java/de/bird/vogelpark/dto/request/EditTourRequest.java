package de.bird.vogelpark.dto.request;

public record EditTourRequest(
        String currentName,
        String newName,
        String newDescription,
        Integer newPriceCents,
        String newImageUrl,
        Integer newStartTimeHour, Integer newStartTimeMinute,
        Integer newEndTimeHour, Integer newEndTimeMinute,
        String[] attractionNamesToAdd,
        String[] attractionNamesToRemove
) {
    public EditTourRequest {
        if (attractionNamesToAdd == null) {
            attractionNamesToAdd = new String[0];
        }
        if (attractionNamesToRemove == null) {
            attractionNamesToRemove = new String[0];
        }
    }
}
