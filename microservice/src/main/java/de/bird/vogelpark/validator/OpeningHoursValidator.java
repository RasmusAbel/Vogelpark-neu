package de.bird.vogelpark.validator;

import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class OpeningHoursValidator {

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

        public boolean isValid(OpeningHoursValidationResult validationResult) {
            return validationResult == OpeningHoursValidationResult.VALID;
        }

        public String getValidationMessage(OpeningHoursValidationResult validationResult) {
            switch(validationResult) {
                case INVALID_HOUR:
                    return "Stundenangaben müssen zwischen 0 und 23 liegen";
                case INVALID_MINUTE:
                    return "Minutenangaben müssen zwischen 0 und 59 liegen";
                case START_TIME_AFTER_END_TIME:
                    return "Startzeit muss vor Endzeit liegen";
                default:
                    return "";
            }
        }
}

