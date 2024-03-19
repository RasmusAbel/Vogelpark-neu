package de.bird.vogelpark.dto.request;

import java.time.LocalTime;

/*
public class CreateOpeningHoursRequest {
    private String weekday;

    private LocalTime startTime;

    private LocalTime endTime;

    public String getWeekday() {
        return weekday;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
 */

public record CreateOpeningHoursRequest(
        String weekday,
        int startTimeHour, int startTimeMinute,
        int endTimeHour, int endTimeMinute) {
    public CreateOpeningHoursRequest {
        if(weekday == null) {
            weekday = "";
        }
    }
}
