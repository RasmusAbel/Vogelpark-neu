package de.bird.vogelpark.service.create;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
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
public class CreateTagServiceTest {

    @InjectMocks
    CreateTagService service;

    @Mock
    FilterTagRepository filterTagRepository;

    private final String FILTER_TAG = "testTag";

    @Test
    public void testCreateTag(){
        when(filterTagRepository.findByName(FILTER_TAG)).thenReturn(Optional.empty());

        ResponseEntity<String> response = service.createTag(FILTER_TAG);

        assertEquals("Tag " + FILTER_TAG + " successfully created", response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testTagExistent(){
        FilterTag foundFilterTag = new FilterTag(FILTER_TAG, new Attraction());

        when(filterTagRepository.findByName(FILTER_TAG)).thenReturn(Optional.of(foundFilterTag));

        ResponseEntity<String> response = service.createTag(FILTER_TAG);

        assertEquals("Tag " + FILTER_TAG + " already exists", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}