package de.bird.vogelpark.utils;


public enum TimeValidationResult {
    VALID,
    INVALID_HOUR,
    INVALID_MINUTE,
    START_TIME_AFTER_END_TIME
}