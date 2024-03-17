package de.bird.vogelpark.dto.response;

import java.util.ArrayList;
import java.util.List;

/*
public class BirdParkBasicInfoResponse {
    private String name;
    private String description;
    private String address;
    private final List<OpeningHoursResponse> openingHoursResponses = new ArrayList<>();
    private String logoUrl;

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

    public List<OpeningHoursResponse> getOpeningHoursResponses() {
        return openingHoursResponses;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
 */

public record BirdParkBasicInfoResponse(
        String name,
        String description,
        String address,
        List<OpeningHoursResponse> openingHoursResponses,
        String logoUrl
) {
    public BirdParkBasicInfoResponse {
        if (openingHoursResponses == null) {
            openingHoursResponses = new ArrayList<>();
        }
    }
}
