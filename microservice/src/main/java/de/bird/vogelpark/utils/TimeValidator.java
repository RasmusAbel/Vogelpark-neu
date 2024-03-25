package de.bird.vogelpark.utils;

import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalTime;

@Service
public class TimeValidator {

    private final String INVALID_HOURS_MESSAGE = "Hour must be between 0 and 23";
    private final String INVALID_MINUTES_MESSAGE = "Minute must be between 0 and 59";
    private final String STARTTIME_AFTER_ENDTIME_MESSAGE = "Start time must be before end time";

    /**
     * Prüft, ob die Stunde und Minute gültige Werte haben.
     * Stunde muss zwischen 0 und 23 liegen.
     * Minute muss zwischen 0 und 59 liegen.
     *
     * @param minute
     * @param hour
     * @return INVALID_HOUR oder INVALID_MINUTE, wenn die Werte ungültig sind, sonst VALID
     */
    public TimeValidationResult validateValueRange(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            return TimeValidationResult.INVALID_HOUR;
        }
        if (minute < 0 || minute > 59) {
            return TimeValidationResult.INVALID_MINUTE;
        }
        return TimeValidationResult.VALID;
    }

    /**
     * Prüft, ob die startTime vor der endTime liegt
     *
     * @param startTime
     * @param endTime
     * @return VALID, wenn startTime vor endTime liegt, sonst START_TIME_AFTER_END_TIME
     */
    public TimeValidationResult validateStartBeforeEnd(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            return TimeValidationResult.START_TIME_AFTER_END_TIME;
        }
        return TimeValidationResult.VALID;
    }

    /**
     * Gibt zurück, ob ein ValidationResult gültig ist
     *
     * @param validationResult
     * @return
     */
    public boolean isValid(TimeValidationResult validationResult) {
        return validationResult == TimeValidationResult.VALID;
    }

    /**
     * Gibt die passende Fehlermeldung zu einem ValidationResult zurück:
     *
     * @param validationResult
     * @return INVALID_HOUR -> "Stundenangaben müssen zwischen 0 und 23 liegen" <br>
     * INVALID_MINUTE -> "Minutenangaben müssen zwischen 0 und 59 liegen" <br>
     * START_TIME_AFTER_END_TIME -> "Startzeit muss vor Endzeit liegen" <br>
     * Wenn ValidationResult VALID oder unbekannt, wird ein leerer String zurückgegeben
     */
    public String getValidationMessage(TimeValidationResult validationResult) {
        return switch (validationResult) {
            case INVALID_HOUR -> INVALID_HOURS_MESSAGE;
            case INVALID_MINUTE -> INVALID_MINUTES_MESSAGE;
            case START_TIME_AFTER_END_TIME -> STARTTIME_AFTER_ENDTIME_MESSAGE;
            default -> null;
        };
    }

    /**
     * Prüft, ob die Werte in einem CreateOpeningHoursRequest gültig sind. <br>
     * Stunden und Minuten müssen zwischen 0 und 23 bzw. 0 und 59 liegen <br>
     * startTime muss vor endTime liegen
     *
     * @param openingHoursRequest
     * @return Message if Time-Input is incorrect
     */
    public String validateOpeningHours(CreateOpeningHoursRequest openingHoursRequest) {
        return validateTimeInput(openingHoursRequest.startTimeHour(), openingHoursRequest.startTimeMinute(),
                openingHoursRequest.endTimeHour(), openingHoursRequest.endTimeMinute());
    }

    /**
     * Prüft, ob die Zeit-Werte gültig sind. <br>
     * Stunden und Minuten müssen zwischen 0 und 23 bzw. 0 und 59 liegen <br>
     * startTime muss vor endTime liegen
     *
     * @param startTimeHours
     * @param startTimeMinutes
     * @param endTimeHours
     * @param endTimeMinutes
     * @return Message if Time-Input is incorrect
     */
    public String validateTimeInput(int startTimeHours, int startTimeMinutes, int endTimeHours, int endTimeMinutes) {
        LocalTime startTime;
        LocalTime endTime;
        try {
            startTime = LocalTime.of(startTimeHours, startTimeMinutes);
            endTime = LocalTime.of(endTimeHours, endTimeMinutes);
        } catch (DateTimeException e) {
            return getValidationMessage(e.getMessage());
        }
        if (startTime.isAfter(endTime)) {
            return STARTTIME_AFTER_ENDTIME_MESSAGE;
        }
        return null;
    }

    /**
     * This method returns a text for the client, according to an
     * incorrect input for hour or minute.
     *
     * @param exceptionMessage
     * @return Message for User
     */
    private String getValidationMessage(String exceptionMessage) {
        if (exceptionMessage.contains("HourOfDay")) {
            return INVALID_HOURS_MESSAGE;
        } else if (exceptionMessage.contains("MinuteOfHour")) {
            return INVALID_MINUTES_MESSAGE;
        }
        return "Invalid Time-Input";
    }
}

