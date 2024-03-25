package de.bird.vogelpark.utils;

import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class TimeValidatorTest {

    private final String INVALID_HOURS_MESSAGE = "Hour must be between 0 and 23";
    private final String INVALID_MINUTES_MESSAGE = "Minute must be between 0 and 59";
    private final String STARTTIME_AFTER_ENDTIME_MESSAGE = "Start time must be before end time";

    @InjectMocks
    TimeValidator timeValidator;

    @Test
    public void testValidateValueRangeValid(){
        TimeValidationResult result = timeValidator.validateValueRange(12,25);
        assertEquals(TimeValidationResult.VALID, result);
    }

    @Test
    public void testValidateValueRangeInvalidHour(){
        TimeValidationResult result = timeValidator.validateValueRange(26,25);
        assertEquals(TimeValidationResult.INVALID_HOUR, result);
    }

    @Test
    public void testValidateValueRangeInvalidMinute(){
        TimeValidationResult result = timeValidator.validateValueRange(22,99);
        assertEquals(TimeValidationResult.INVALID_MINUTE, result);
    }

    @Test
    public void testValidateStartBeforeEndValid(){
        TimeValidationResult result = timeValidator.validateStartBeforeEnd(LocalTime.of(9,0),LocalTime.of(12,0));
        assertEquals(TimeValidationResult.VALID, result);
    }

    @Test
    public void testValidateStartBeforeEndInvalid(){
        TimeValidationResult result = timeValidator.validateStartBeforeEnd(LocalTime.of(9,0),LocalTime.of(8,0));
        assertEquals(TimeValidationResult.START_TIME_AFTER_END_TIME, result);
    }

    @Test
    public void testGetValidationMessageValidationResultInvalidHour(){
        String result = timeValidator.getValidationMessage(TimeValidationResult.INVALID_HOUR);
        assertEquals(INVALID_HOURS_MESSAGE, result);
    }

    @Test
    public void testGetValidationMessageValidationResultInvalidMinute(){
        String result = timeValidator.getValidationMessage(TimeValidationResult.INVALID_MINUTE);
        assertEquals(INVALID_MINUTES_MESSAGE, result);
    }

    @Test
    public void testGetValidationMessageValidationResultStartTimeAfterEndTime(){
        String result = timeValidator.getValidationMessage(TimeValidationResult.START_TIME_AFTER_END_TIME);
        assertEquals(STARTTIME_AFTER_ENDTIME_MESSAGE, result);
    }

    @Test
    public void testGetValidationMessageValidationResultValid(){
        String result = timeValidator.getValidationMessage(TimeValidationResult.VALID);
        assertNull(result);
    }

    @Test
    public void testValidateOpeningHours(){
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest("Weekday", 9,0,12,0);
        String result = timeValidator.validateOpeningHours(openingHoursRequest);
        assertNull(result);
    }

    @Test
    public void testValidateTimeInputValid(){
        String result = timeValidator.validateTimeInput(9,0,12,0);
        assertNull(result);
    }

    @Test
    public void testValidateTimeInputInvalidHours(){
        String result = timeValidator.validateTimeInput(9,0,99,0);
        assertEquals(INVALID_HOURS_MESSAGE, result);
    }

    @Test
    public void testValidateTimeInputInvalidMinutes(){
        String result = timeValidator.validateTimeInput(9,0,12,99);
        assertEquals(INVALID_MINUTES_MESSAGE, result);
    }

    @Test
    public void testValidateTimeInputStartTimeAfterEndTime(){
        String result = timeValidator.validateTimeInput(18,0,12,0);
        assertEquals(STARTTIME_AFTER_ENDTIME_MESSAGE, result);
    }

}
