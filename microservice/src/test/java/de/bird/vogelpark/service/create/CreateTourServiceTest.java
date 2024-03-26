package de.bird.vogelpark.service.create;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.dto.request.CreateTourRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.TourRepository;
import de.bird.vogelpark.utils.TimeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateTourServiceTest {

    @InjectMocks
    CreateTourService service;

    @Mock
    TourRepository tourRepository;

    @Mock
    TimeValidator timeValidator;

    @Mock
    AttractionRepository attractionRepository;

    @Test
    public void testCreateTour(){
        String[] attractionNames = {"attraction"};
        CreateTourRequest request = new CreateTourRequest("name", "description", 2555, "imageUrl", 9,0,12,0, attractionNames);
        Attraction attraction = new Attraction();


        when(tourRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(timeValidator.validateTimeInput(request.startTimeHour(), request.startTimeMinute(), request.endTimeHour(), request.endTimeMinute())).thenReturn(null);
        when(attractionRepository.findByName(attractionNames[0])).thenReturn(Optional.of(attraction));

        ResponseEntity<String> response = service.createTour(request);

        assertEquals("Tour " + request.name() + " successfully created", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateExistentTour(){
        String[] attractionNames = {"attraction"};
        CreateTourRequest request = new CreateTourRequest("name", "description", 2555, "imageUrl", 9,0,12,0, attractionNames);
        Tour tour = new Tour();

        when(tourRepository.findByName(request.name())).thenReturn(Optional.of(tour));

        ResponseEntity<String> response = service.createTour(request);

        assertEquals("Tour " + request.name() + " already exists", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddNotExistentAttraction(){
        String[] attractionNames = {"attraction"};
        CreateTourRequest request = new CreateTourRequest("name", "description", 2555, "imageUrl", 9,0,12,0, attractionNames);

        when(tourRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(timeValidator.validateTimeInput(request.startTimeHour(), request.startTimeMinute(), request.endTimeHour(), request.endTimeMinute())).thenReturn(null);
        when(attractionRepository.findByName(attractionNames[0])).thenReturn(Optional.empty());

        ResponseEntity<String> response = service.createTour(request);

        assertEquals("Attraction with name " + attractionNames[0] + " does not exist and therefore cannot be added to tour " + request.name(), response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testInvalidTimeInput(){
        String[] attractionNames = {"attraction"};
        CreateTourRequest request = new CreateTourRequest("name", "description", 2555, "imageUrl", 30,0,12,0, attractionNames);

        when(tourRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(timeValidator.validateTimeInput(request.startTimeHour(), request.startTimeMinute(), request.endTimeHour(), request.endTimeMinute())).thenReturn("Start time must be before end time");

        ResponseEntity<String> response = service.createTour(request);

        assertEquals("Start time must be before end time", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
