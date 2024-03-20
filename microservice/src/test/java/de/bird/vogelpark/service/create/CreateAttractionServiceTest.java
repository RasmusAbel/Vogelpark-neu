package de.bird.vogelpark.service.create;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.dto.request.CreateAttractionRequest;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import de.bird.vogelpark.validator.OpeningHoursValidationResult;
import de.bird.vogelpark.validator.OpeningHoursValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateAttractionServiceTest {

    @InjectMocks
    CreateAttractionService service;

    @Mock
    AttractionRepository attractionRepository;

    @Mock
    OpeningHoursRepository openingHoursRepository;

    @Mock
    FilterTagRepository filterTagRepository;

    @Mock
    OpeningHoursValidator openingHoursValidator;


    private final String DESCRIPTION = "description";
    private final String NAME = "name";
    private final String WEEKDAY = "weekday";
    private final String FILTER_TAG = "filterTag";

    @Test
    public void testCreateAttraction(){
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest(WEEKDAY, 9,0,12,0);
        CreateOpeningHoursRequest[] openingHoursRequestArray = {openingHoursRequest};
        String[] filterTags = {FILTER_TAG};
        CreateAttractionRequest request = new CreateAttractionRequest(NAME, DESCRIPTION, openingHoursRequestArray, filterTags);

        when(attractionRepository.findByName(NAME)).thenReturn(Optional.empty());
        when(openingHoursRepository.save(any())).thenReturn(null);
        when(filterTagRepository.save(any())).thenReturn(null);
        when(openingHoursValidator.validate(openingHoursRequest)).thenReturn(OpeningHoursValidationResult.VALID);
        when(openingHoursValidator.isValid(OpeningHoursValidationResult.VALID)).thenReturn(true);

        ResponseEntity<String> response = service.createAttraction(request);

        assertEquals("Attraction " + NAME + " successfully created", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAttractionExistent(){
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest(WEEKDAY, 9,0,12,0);
        CreateOpeningHoursRequest[] openingHoursRequestArray = {openingHoursRequest};
        String[] filterTags = {FILTER_TAG};
        CreateAttractionRequest request = new CreateAttractionRequest(NAME, DESCRIPTION, openingHoursRequestArray, filterTags);
        Attraction attraction = new Attraction();

        when(attractionRepository.findByName(NAME)).thenReturn(Optional.of(attraction));

        ResponseEntity<String> response = service.createAttraction(request);

        assertEquals("Attraction " + NAME + " already exists", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}