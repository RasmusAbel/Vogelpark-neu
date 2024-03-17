package de.bird.vogelpark.dto.response;

/*
public class OpeningHoursResponse {
    private Long id;
    private String weekday;
    private String startTime;
    private String endTime;

    public OpeningHoursResponse(Long id, String weekday, String startTime, String endTime) {
        this.id = id;
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
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
 */

public record OpeningHoursResponse(
        Long id,
        String weekday,
        String startTime,
        String endTime
) {}
