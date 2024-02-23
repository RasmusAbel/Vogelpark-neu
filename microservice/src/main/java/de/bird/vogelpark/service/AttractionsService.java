package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.FilterTagResponse;
import de.bird.vogelpark.dto.OpeningHoursResponse;
import de.bird.vogelpark.dto.ReadAttractionsByTagsResponse;
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

    public List<ReadAttractionsByTagsResponse> readAttractionsByTags(List<String> tags) {
        List<Attraction> data = attractionRepository.findAttractionsByTags(tags, tags.size());
        return mapData2Response(data);
    }

    private List<ReadAttractionsByTagsResponse> mapData2Response(List<Attraction> data) {
        List<ReadAttractionsByTagsResponse> responses = new ArrayList<>();

        for(Attraction attraction : data) {
            ReadAttractionsByTagsResponse nextResponse = new ReadAttractionsByTagsResponse();
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
