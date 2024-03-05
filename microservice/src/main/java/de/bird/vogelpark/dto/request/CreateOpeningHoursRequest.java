package de.bird.vogelpark.dto.request;

public class CreateOpeningHoursRequest {
    private String weekday;

    private String startTime;

    private String endTime;

    public String getWeekday() {
        return weekday;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
