package de.bird.vogelpark.service.read;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.dto.response.ReadTourResponse;
import de.bird.vogelpark.repositories.TourRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadToursServiceTest {

    @InjectMocks
    ReadToursService service;

    @Mock
    TourRepository tourRepository;

    @Test
    public void testReadAllTours(){
        List<Tour> tours = new ArrayList<>();
        Tour tour1 = new Tour();
        tour1.setName("Tour1");
        Tour tour2 = new Tour();
        tour2.setName("Tour2");
        Set<Attraction> tourAttractions = new HashSet<>();
        Attraction attraction = new Attraction();
        attraction.setName("Attraktion1");
        tourAttractions.add(attraction);
        tour1.setAttractions(tourAttractions);
        tour2.setAttractions(tourAttractions);
        tours.add(tour1);
        tours.add(tour2);

        when(tourRepository.findAll()).thenReturn(tours);

        List<ReadTourResponse> response = service.readAllTours();

        assertEquals(2, response.size());
        assertEquals(tour1.getName(), response.get(0).name());
        assertTrue(response.get(0).attractionNames().contains(attraction.getName()));
    }

    @Test
    public void testReadToursByAttractionNames() {
        List<Tour> tours = new ArrayList<>();
        Tour tour1 = new Tour();
        tour1.setName("Tour1");
        Set<Attraction> tourAttractions = new HashSet<>();
        Attraction attraction = new Attraction();
        attraction.setName("Attraktion1");
        tourAttractions.add(attraction);
        tour1.setAttractions(tourAttractions);
        tours.add(tour1);

        List<String> attractionNames = new ArrayList<>();
        attractionNames.add("Attraktion1");

        when(tourRepository.findAll()).thenReturn(tours);

        List<ReadTourResponse> response = service.readToursByAttractionNames(attractionNames);

        assertEquals(1, response.size());
        assertEquals(tour1.getName(), response.get(0).name());

    }




}