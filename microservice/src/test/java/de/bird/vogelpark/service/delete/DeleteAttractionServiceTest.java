package de.bird.vogelpark.service.delete;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.repositories.AttractionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteAttractionServiceTest {

    @InjectMocks
    DeleteAttractionService service;

    @Mock
    AttractionRepository attractionRepository;

    private final String ATTRACTION_NAME = "testAttraction";

    @Test
    public void testDeleteAttraction(){
        Attraction attraction = new Attraction();
        attraction.setName(ATTRACTION_NAME);

        when(attractionRepository.findByName(ATTRACTION_NAME)).thenReturn(Optional.of(attraction));

        ResponseEntity<String> response = service.deleteAttraction(ATTRACTION_NAME);

        assertEquals("Attraction " + ATTRACTION_NAME + " successfully deleted", response.getBody());
    }

    @Test
    public void testDeleteNotExistentAttraction(){
        when(attractionRepository.findByName(ATTRACTION_NAME)).thenReturn(Optional.empty());

        ResponseEntity<String> response = service.deleteAttraction(ATTRACTION_NAME);

        assertEquals("Attraction " + ATTRACTION_NAME + " does not exist and can't be deleted", response.getBody());
    }

}