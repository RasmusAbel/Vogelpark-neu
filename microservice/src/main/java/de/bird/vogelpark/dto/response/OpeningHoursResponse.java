package de.bird.vogelpark.dto.response;

public class OpeningHoursResponse {
    private String weekday;

    private String startTime;

    private String endTime;

    public OpeningHoursResponse(String weekday, String startTime, String endTime) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
    }

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
