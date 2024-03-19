package de.bird.vogelpark.service.read;

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
public class ReadAttractionsService {
    private AttractionRepository attractionRepository;

    public ReadAttractionsService(AttractionRepository attractionRepository) {
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

            List<OpeningHoursResponse> openingHoursResponses = new ArrayList<>();
            for(OpeningHours openingHoursData : attraction.getOpeningHours()) {
                openingHoursResponses.add(new OpeningHoursResponse(
                        openingHoursData.getId(),
                        openingHoursData.getWeekday(),
                        openingHoursData.getStartTime(),
                        openingHoursData.getEndTime())
                );
            }

            List<String> filterTagResponses = new ArrayList<>();
            for (FilterTag tag : attraction.getFilterTags()) {
                filterTagResponses.add(tag.getName());
            }

            ReadAttractionsResponse nextResponse = new ReadAttractionsResponse(
                    attraction.getName(),
                    attraction.getDescription(),
                    attraction.getImageUrl(),
                    openingHoursResponses,
                    filterTagResponses
            );

            responses.add(nextResponse);
        }

        return responses;
    }
}
