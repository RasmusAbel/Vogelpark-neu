package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.response.OpeningHoursResponse;
import de.bird.vogelpark.dto.response.ReadAttractionsResponse;
import de.bird.vogelpark.repositories.AttractionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttractionsService {
    private AttractionRepository attractionRepository;

    public AttractionsService(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    public List<ReadAttractionsResponse> readAttractionsByTags(List<String> tags) {
        List<Attraction> data = attractionRepository.findAttractionsByTags(tags, tags.size());
        return mapData2Response(data);
    }

    public List<ReadAttractionsResponse> readAllAttractions() {
        List<Attraction> data = new ArrayList<>();
        for(Attraction attr : attractionRepository.findAll()) {
            data.add(attr);
        }

        return mapData2Response(data);
    }

    private List<ReadAttractionsResponse> mapData2Response(List<Attraction> data) {
        List<ReadAttractionsResponse> responses = new ArrayList<>();

        for(Attraction attraction : data) {
            ReadAttractionsResponse nextResponse = new ReadAttractionsResponse();
            nextResponse.setName(attraction.getName());
            nextResponse.setDescription(attraction.getDescription());

            for(OpeningHours openingHours : attraction.getOpeningHours()) {
                nextResponse.getOpeningHoursResponses().add(new OpeningHoursResponse(
                        openingHours.getWeekday(),
                        openingHours.getStartTime(),
                        openingHours.getEndTime())
                );
            }

            for (FilterTag tag : attraction.getFilterTags()) {
                nextResponse.getFilterTagResponses().add(tag.getName());
            }

            responses.add(nextResponse);
        }

        return responses;
    }
}
