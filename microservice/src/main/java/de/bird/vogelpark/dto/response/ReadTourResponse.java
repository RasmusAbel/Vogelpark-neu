package de.bird.vogelpark.dto.response;

import java.util.HashSet;
import java.util.Set;

public record ReadTourResponse(
        String name,
        String description,
        int price,
        String startTime,
        String endTime,
        String duration,
        String imageUrl,
        Set<String> attractionNames
) {
    public ReadTourResponse {
        if (attractionNames == null) {
            attractionNames = new HashSet<>();
        }
    }
}


/*
import java.util.HashSet;
import java.util.Set;

public class ReadTourResponse {
    private String name;
    private String description;
    private int price;
    private String startTime;
    private String endTime;
    private String duration;
    private String imageUrl;
    private Set<String> attractionNames = new HashSet<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<String> getAttractionNames() {
        return attractionNames;
    }
}

 */
