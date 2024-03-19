package de.bird.vogelpark.validator;


public enum OpeningHoursValidationResult {
    VALID,
    INVALID_HOUR,
    INVALID_MINUTE,
    START_TIME_AFTER_END_TIME
}