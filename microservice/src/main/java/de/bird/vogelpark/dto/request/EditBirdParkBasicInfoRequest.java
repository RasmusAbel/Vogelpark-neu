package de.bird.vogelpark.dto.request;

/*
public class EditBirdParkBasicInfoRequest {
    public String newName;
    public String newAddress;
    public String newDescription;
    public CreateOpeningHoursRequest[] openingHoursToAdd;
    public Long[] openingHourIdsToRemove;

    public String newLogoUrl;

    public String getNewName() {
        return newName;
    }

    public String getNewAddress() {
        return newAddress;
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

    public String getNewLogoUrl() {
        return newLogoUrl;
    }
}
 */

public record EditBirdParkBasicInfoRequest(
        String newName,
        String newAddress,
        String newDescription,
        CreateOpeningHoursRequest[] openingHoursToAdd,
        Long[] openingHourIdsToRemove,
        String newLogoUrl) {
    public EditBirdParkBasicInfoRequest {
        if(openingHoursToAdd == null) {
            openingHoursToAdd = new CreateOpeningHoursRequest[0];
        }
        if(openingHourIdsToRemove == null) {
            openingHourIdsToRemove = new Long[0];
        }
    }
}
