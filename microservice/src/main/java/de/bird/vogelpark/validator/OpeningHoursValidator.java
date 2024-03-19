package de.bird.vogelpark.validator;

import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class OpeningHoursValidator {
    /**
     * Prüft, ob die Werte in einem CreateOpeningHoursRequest gültig sind. <br>
     * Stunden und Minuten müssen zwischen 0 und 23 bzw. 0 und 59 liegen <br>
     * startTime muss vor endTime liegen
     * @param req
     * @return
     */
    public OpeningHoursValidationResult validate(CreateOpeningHoursRequest req) {
        if(req.startTimeHour() < 0 || req.startTimeHour() > 23) {
            return OpeningHoursValidationResult.INVALID_HOUR;
        }
        if(req.startTimeMinute() < 0 || req.startTimeMinute() > 59) {
            return OpeningHoursValidationResult.INVALID_MINUTE;
        }
        if(req.endTimeHour() < 0 || req.endTimeHour() > 23) {
            return OpeningHoursValidationResult.INVALID_HOUR;
        }
        if(req.endTimeMinute() < 0 || req.endTimeMinute() > 59) {
            return OpeningHoursValidationResult.INVALID_MINUTE;
        }

        LocalTime startTime = LocalTime.of(req.startTimeHour(), req.startTimeMinute());
        LocalTime endTime = LocalTime.of(req.endTimeHour(), req.endTimeMinute());
        if(startTime.isAfter(endTime)) {
            return OpeningHoursValidationResult.START_TIME_AFTER_END_TIME;
        }

        return OpeningHoursValidationResult.VALID;
    }

    /**
     * Prüft, ob die Stunde und Minute gültige Werte haben.
     * Stunde muss zwischen 0 und 23 liegen.
     * Minute muss zwischen 0 und 59 liegen.
     * @param minute
     * @param hour
     * @return INVALID_HOUR oder INVALID_MINUTE, wenn die Werte ungültig sind, sonst VALID
     */
    public OpeningHoursValidationResult validateValueRange(int hour, int minute) {
        if(hour < 0 || hour > 23) {
            return OpeningHoursValidationResult.INVALID_HOUR;
        }
        if(minute < 0 || minute > 59) {
            return OpeningHoursValidationResult.INVALID_MINUTE;
        }
        return OpeningHoursValidationResult.VALID;
    }

    /**
     * Prüft, ob die startTime vor der endTime liegt
     * @param startTime
     * @param endTime
     * @return VALID, wenn startTime vor endTime liegt, sonst START_TIME_AFTER_END_TIME
     */
    public OpeningHoursValidationResult validateStartBeforeEnd(LocalTime startTime, LocalTime endTime) {
        if(startTime.isAfter(endTime)) {
            return OpeningHoursValidationResult.START_TIME_AFTER_END_TIME;
        }
        return OpeningHoursValidationResult.VALID;
    }

    /**
     * Gibt zurück, ob ein ValidationResult gültig ist
     * @param validationResult
     * @return
     */
    public boolean isValid(OpeningHoursValidationResult validationResult) {
        return validationResult == OpeningHoursValidationResult.VALID;
    }

    /**
     * Gibt die passende Fehlermeldung zu einem ValidationResult zurück:
     * @param validationResult
     * @return
     * INVALID_HOUR -> "Stundenangaben müssen zwischen 0 und 23 liegen" <br>
     * INVALID_MINUTE -> "Minutenangaben müssen zwischen 0 und 59 liegen" <br>
     * START_TIME_AFTER_END_TIME -> "Startzeit muss vor Endzeit liegen" <br>
     * Wenn ValidationResult VALID oder unbekannt, wird ein leerer String zurückgegeben
     */
    public String getValidationMessage(OpeningHoursValidationResult validationResult) {
        switch(validationResult) {
            case INVALID_HOUR:
                return "Hour must be between 0 and 23";
            case INVALID_MINUTE:
                return "Minute must be between 0 and 59";
            case START_TIME_AFTER_END_TIME:
                return "Start time must be before end time";
            default:
                return "";
        }
    }
}

