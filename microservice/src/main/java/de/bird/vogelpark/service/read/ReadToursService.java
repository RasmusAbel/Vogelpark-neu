package de.bird.vogelpark.service.read;

import de.bird.vogelpark.BirdParkApplication;
import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.dto.response.ReadTourResponse;
import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.repositories.TourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReadToursService {
    TourRepository tourRepository;

    private static final Logger logger = LoggerFactory.getLogger(BirdParkApplication.class);

    public ReadToursService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<ReadTourResponse> readAllTours() {
        List<Tour> data = new ArrayList<>();

        for(Tour tour : tourRepository.findAll()) {
            data.add(tour);
        }

        return mapData2Response(data);
    }

    private List<ReadTourResponse> mapData2Response(List<Tour> data) {
        List<ReadTourResponse> responses = new ArrayList<>();

        for(Tour tour : data) {

            Set<String> attractionNames = new HashSet<>();
            for(Attraction attr : tour.getAttractions()) {
                attractionNames.add(attr.getName());
            }

            ReadTourResponse nextResponse = new ReadTourResponse(
                    tour.getName(),
                    tour.getDescription(),
                    tour.getPriceCents(),
                    tour.getStartTime(),
                    tour.getEndTime(),
                    tour.getDuration(),
                    tour.getImageUrl(),
                    attractionNames
            );

            logger.info("--- tour name: " + nextResponse.name());
            logger.info("--- tour desc: " + nextResponse.description());

            /*
            nextResponse.setName(tour.getName());
            nextResponse.setDescription(tour.getDescription());
            nextResponse.setPrice(tour.getPriceCents());
            nextResponse.setStartTime(tour.getStartTime());
            nextResponse.setEndTime(tour.getEndTime());
            nextResponse.setDuration(tour.getDuration());
            nextResponse.setImageUrl(tour.getImageUrl());

            for(Attraction attr : tour.getAttractions()) {
                nextResponse.getAttractionNames().add(attr.getName());
            }
             */

            responses.add(nextResponse);
        }

        return responses;
    }
}