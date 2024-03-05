package de.bird.vogelpark.dto.response;

import java.util.ArrayList;
import java.util.List;

public class BirdParkBasicInfoResponse {
    private String name;
    private String description;
    private String address;
    private final List<OpeningHoursResponse> openingHoursResponses = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public List<OpeningHoursResponse> getOpeningHoursResponses() {
        return openingHoursResponses;
    }
}
