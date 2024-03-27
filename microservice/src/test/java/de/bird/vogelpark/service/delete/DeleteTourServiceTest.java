package de.bird.vogelpark.service.delete;

import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.repositories.TourRepository;
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
public class DeleteTourServiceTest {

    @InjectMocks
    DeleteTourService service;

    @Mock
    TourRepository tourRepository;

    private final String TOUR_NAME = "testTour";

    @Test
    public void testDeleteTour(){
        Tour tour = new Tour();
        tour.setName(TOUR_NAME);

        when(tourRepository.findByName(TOUR_NAME)).thenReturn(Optional.of(tour));

        ResponseEntity<String> response = service.deleteTour(TOUR_NAME);

        assertEquals("Tour " + TOUR_NAME + " successfully deleted", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteNotExistentTour(){
        when(tourRepository.findByName(TOUR_NAME)).thenReturn(Optional.empty());

        ResponseEntity<String> response = service.deleteTour(TOUR_NAME);

        assertEquals("Tour " + TOUR_NAME + " does not exist and can't be deleted", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
