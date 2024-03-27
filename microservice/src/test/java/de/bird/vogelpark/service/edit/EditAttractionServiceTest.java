package de.bird.vogelpark.service.edit;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.dto.request.EditAttractionRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
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
public class EditAttractionServiceTest {

    @InjectMocks
    EditAttractionService service;

    @Mock
    AttractionRepository attractionRepository;

    @Mock
    OpeningHoursRepository openingHoursRepository;

    @Mock
    FilterTagRepository filterTagRepository;

    private final String WEEKDAY = "Monday";
    private final String CURRENT_NAME = "CurrentName";
    private final String NEW_NAME = "NewName";
    private final String NEW_DESCRIPTION = "NewDescription";
    private final String NEW_IMAGE_URL = "NewImageUrl";
    private final String[] TAGS_TO_ADD = {"FilterTagToAdd"};
    private final String[] TAGS_TO_REMOVE = {"FilterTagToRemove"};
    private final Long[] OPENING_HOURS_IDS_TO_REMOVE = {1234L};

    private final String SERVICE_SUCCESS = String.format("Attraction %s successfully edited", CURRENT_NAME);

    /**
     * Testing all Edit-Functions
     */
    @Test
    public void testEditAttraction() {
        Attraction attraction = new Attraction();
        OpeningHours openingHoursToRemove = new OpeningHours();
        FilterTag filterTagToRemove = new FilterTag(TAGS_TO_REMOVE[0], attraction);
        attraction.getOpeningHours().add(openingHoursToRemove);
        attraction.getFilterTags().add(filterTagToRemove);
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest(WEEKDAY, 9,0,12,0);
        EditAttractionRequest req = new EditAttractionRequest(CURRENT_NAME, NEW_NAME, NEW_DESCRIPTION, NEW_IMAGE_URL, new CreateOpeningHoursRequest[]{openingHoursRequest}, OPENING_HOURS_IDS_TO_REMOVE, TAGS_TO_ADD, TAGS_TO_REMOVE);

        when(attractionRepository.findByName(req.currentName())).thenReturn(Optional.of(attraction));
        when(openingHoursRepository.findById(OPENING_HOURS_IDS_TO_REMOVE[0])).thenReturn(Optional.of(openingHoursToRemove));
        ResponseEntity<String> response = service.editAttraction(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SERVICE_SUCCESS, response.getBody());
    }

    /**
     * Testing to edit a not existent attraction
     */
    @Test
    public void testAttractionNotExistent() {
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest(WEEKDAY, 9,0,12,0);
        EditAttractionRequest req = new EditAttractionRequest(CURRENT_NAME, NEW_NAME, NEW_DESCRIPTION, NEW_IMAGE_URL, new CreateOpeningHoursRequest[]{openingHoursRequest}, OPENING_HOURS_IDS_TO_REMOVE, TAGS_TO_ADD, TAGS_TO_REMOVE);

        when(attractionRepository.findByName(req.currentName())).thenReturn(Optional.empty());

        ResponseEntity<String> response = service.editAttraction(req);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(String.format("Attraction with name %s does not exist", CURRENT_NAME), response.getBody());
    }

    /**
     * Testing to add an existent tag
     */
    @Test
    public void testFilterTagExistent(){
        Attraction attraction = new Attraction();
        OpeningHours openingHoursToRemove = new OpeningHours();
        FilterTag existentFilterTag = new FilterTag(TAGS_TO_ADD[0], attraction);
        attraction.getOpeningHours().add(openingHoursToRemove);
        attraction.getFilterTags().add(existentFilterTag);
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest(WEEKDAY, 9,0,12,0);
        EditAttractionRequest req = new EditAttractionRequest(CURRENT_NAME, NEW_NAME, NEW_DESCRIPTION, NEW_IMAGE_URL, new CreateOpeningHoursRequest[]{openingHoursRequest}, OPENING_HOURS_IDS_TO_REMOVE, TAGS_TO_ADD, TAGS_TO_REMOVE);

        when(attractionRepository.findByName(req.currentName())).thenReturn(Optional.of(attraction));
        when(openingHoursRepository.findById(OPENING_HOURS_IDS_TO_REMOVE[0])).thenReturn(Optional.of(openingHoursToRemove));
        ResponseEntity<String> response = service.editAttraction(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SERVICE_SUCCESS, response.getBody());
    }


}