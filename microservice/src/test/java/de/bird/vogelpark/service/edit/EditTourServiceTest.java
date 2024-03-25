package de.bird.vogelpark.service.edit;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.dto.request.EditTourRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.TourRepository;
import de.bird.vogelpark.utils.TimeValidationResult;
import de.bird.vogelpark.utils.TimeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EditTourServiceTest {

    private final String CURRENT_NAME = "CurrentName";
    private final String NEW_NAME = "NewName";
    private final String NEW_DESCRIPTION = "NewDescription";
    private final String NEW_IMAGE_URL = "NewImageUrl";
    private final int NEW_PRICE_CENTS = 15;
    private final String[] ATTRACTIONS_TO_ADD = {"AttractionToAdd"};
    private final String[] ATTRACTIONS_TO_REMOVE = {"AttractionToRemove"};

    private final String SERVICE_SUCCESS = String.format("Tour %s successfully edited", CURRENT_NAME);

    @InjectMocks
    EditTourService service;

    @Mock
    TourRepository tourRepository;

    @Mock
    TimeValidator timeValidator;

    @Mock
    AttractionRepository attractionRepository;

    @Test
    public void testEditTour() {
        int startTimeHour = 9;
        int startTimeMinute = 0;
        int endTimeHour = 12;
        int endTimeMinute = 0;


        EditTourRequest req = new EditTourRequest(CURRENT_NAME, NEW_NAME, NEW_DESCRIPTION, NEW_PRICE_CENTS, NEW_IMAGE_URL, startTimeHour,startTimeMinute,endTimeHour,endTimeMinute, ATTRACTIONS_TO_ADD, ATTRACTIONS_TO_REMOVE);
        Tour tour = new Tour();
        Attraction attraction = new Attraction();

        when(tourRepository.findByName(req.currentName())).thenReturn(Optional.of(tour));
        when(timeValidator.validateValueRange(anyInt(), anyInt())).thenReturn(TimeValidationResult.VALID);
        when(timeValidator.validateStartBeforeEnd(any(), any())).thenReturn(TimeValidationResult.VALID);
        when(timeValidator.isValid(TimeValidationResult.VALID)).thenReturn(true);
        when(attractionRepository.findByName(ATTRACTIONS_TO_ADD[0])).thenReturn(Optional.of(attraction));
        ResponseEntity<String> response = service.editTour(req);

        assertEquals(SERVICE_SUCCESS, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testEditNotExistentTour() {
        int startTimeHour = 9;
        int startTimeMinute = 0;
        int endTimeHour = 12;
        int endTimeMinute = 0;

        EditTourRequest req = new EditTourRequest(CURRENT_NAME, NEW_NAME, NEW_DESCRIPTION, NEW_PRICE_CENTS, NEW_IMAGE_URL, startTimeHour,startTimeMinute,endTimeHour,endTimeMinute, ATTRACTIONS_TO_ADD, ATTRACTIONS_TO_REMOVE);

        when(tourRepository.findByName(req.currentName())).thenReturn(Optional.empty());
        ResponseEntity<String> response = service.editTour(req);

        assertEquals("Tour with name " + req.currentName() + " does not exist", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testInvalidHours() {
        final String INVALID_HOURS_MESSAGE = "Hour must be between 0 and 23";
        int startTimeHour = 99;
        int startTimeMinute = 0;
        int endTimeHour = 12;
        int endTimeMinute = 0;

        EditTourRequest req = new EditTourRequest(CURRENT_NAME, NEW_NAME, NEW_DESCRIPTION, NEW_PRICE_CENTS, NEW_IMAGE_URL, startTimeHour,startTimeMinute,endTimeHour,endTimeMinute, ATTRACTIONS_TO_ADD, ATTRACTIONS_TO_REMOVE);
        Tour tour = new Tour();

        when(tourRepository.findByName(req.currentName())).thenReturn(Optional.of(tour));
        when(timeValidator.validateValueRange(startTimeHour, startTimeMinute)).thenReturn(TimeValidationResult.INVALID_HOUR);
        when(timeValidator.getValidationMessage(TimeValidationResult.INVALID_HOUR)).thenReturn(INVALID_HOURS_MESSAGE);
        ResponseEntity<String> response = service.editTour(req);

        assertEquals(INVALID_HOURS_MESSAGE, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testInvalidMinutes() {
        final String INVALID_MINUTES_MESSAGE = "Minute must be between 0 and 59";
        int startTimeHour = 8;
        int startTimeMinute = 0;
        int endTimeHour = 12;
        int endTimeMinute = 99;

        EditTourRequest req = new EditTourRequest(CURRENT_NAME, NEW_NAME, NEW_DESCRIPTION, NEW_PRICE_CENTS, NEW_IMAGE_URL, startTimeHour,startTimeMinute,endTimeHour,endTimeMinute, ATTRACTIONS_TO_ADD, ATTRACTIONS_TO_REMOVE);
        Tour tour = new Tour();

        when(tourRepository.findByName(req.currentName())).thenReturn(Optional.of(tour));
        when(timeValidator.validateValueRange(startTimeHour, startTimeMinute)).thenReturn(TimeValidationResult.VALID);
        when(timeValidator.validateValueRange(endTimeHour, endTimeMinute)).thenReturn(TimeValidationResult.INVALID_MINUTE);
        when(timeValidator.isValid(TimeValidationResult.VALID)).thenReturn(true);
        when(timeValidator.getValidationMessage(TimeValidationResult.INVALID_MINUTE)).thenReturn(INVALID_MINUTES_MESSAGE);
        ResponseEntity<String> response = service.editTour(req);

        assertEquals(INVALID_MINUTES_MESSAGE, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testStartTimeAfterEndTime() {
        final String STARTTIME_AFTER_ENDTIME_MESSAGE = "Start time must be before end time";
        int startTimeHour = 18;
        int startTimeMinute = 0;
        int endTimeHour = 12;
        int endTimeMinute = 0;

        EditTourRequest req = new EditTourRequest(CURRENT_NAME, NEW_NAME, NEW_DESCRIPTION, NEW_PRICE_CENTS, NEW_IMAGE_URL, startTimeHour,startTimeMinute,endTimeHour,endTimeMinute, ATTRACTIONS_TO_ADD, ATTRACTIONS_TO_REMOVE);
        Tour tour = new Tour();

        when(tourRepository.findByName(req.currentName())).thenReturn(Optional.of(tour));
        when(timeValidator.validateValueRange(anyInt(), anyInt())).thenReturn(TimeValidationResult.VALID);
        when(timeValidator.isValid(TimeValidationResult.VALID)).thenReturn(true);
        when(timeValidator.validateStartBeforeEnd(LocalTime.of(startTimeHour, startTimeMinute), LocalTime.of(endTimeHour,endTimeMinute))).thenReturn(TimeValidationResult.START_TIME_AFTER_END_TIME);
        when(timeValidator.getValidationMessage(TimeValidationResult.START_TIME_AFTER_END_TIME)).thenReturn(STARTTIME_AFTER_ENDTIME_MESSAGE);
        ResponseEntity<String> response = service.editTour(req);

        assertEquals(STARTTIME_AFTER_ENDTIME_MESSAGE, response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAttractionToAddNotExistent() {
        int startTimeHour = 9;
        int startTimeMinute = 0;
        int endTimeHour = 12;
        int endTimeMinute = 0;


        EditTourRequest req = new EditTourRequest(CURRENT_NAME, NEW_NAME, NEW_DESCRIPTION, NEW_PRICE_CENTS, NEW_IMAGE_URL, startTimeHour,startTimeMinute,endTimeHour,endTimeMinute, ATTRACTIONS_TO_ADD, ATTRACTIONS_TO_REMOVE);
        Tour tour = new Tour();

        when(tourRepository.findByName(req.currentName())).thenReturn(Optional.of(tour));
        when(timeValidator.validateValueRange(anyInt(), anyInt())).thenReturn(TimeValidationResult.VALID);
        when(timeValidator.validateStartBeforeEnd(any(), any())).thenReturn(TimeValidationResult.VALID);
        when(timeValidator.isValid(TimeValidationResult.VALID)).thenReturn(true);
        when(attractionRepository.findByName(ATTRACTIONS_TO_ADD[0])).thenReturn(Optional.empty());
        ResponseEntity<String> response = service.editTour(req);

        assertEquals("Attraction with name " + ATTRACTIONS_TO_ADD[0] + " does not exist and therefore cannot be added to tour " + req.newName(), response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }




}
