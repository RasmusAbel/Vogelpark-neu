package de.bird.vogelpark.beans;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String weekday;

    @Column
    private int startTimeHour, startTimeMinute;

    @Column
    private int endTimeHour, endTimeMinute;

    @ManyToOne(optional = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "bird_park_id")
    //Fremdschlüssel auf den Vogelpark, dessen Öffnungszeiten angegeben werden
    private BirdParkBasicInfo birdParkInfo;

    @ManyToOne(optional = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "attraction_id")
    //ODER Fremdschlüssel auf die Attraktion, deren Öffnungszeiten angegeben werden
    private Attraction attraction;

    public OpeningHours(String weekday, LocalTime startTime, LocalTime endTime, BirdParkBasicInfo birdParkInfo, Attraction attraction) {
        this.weekday = weekday;
        this.startTimeHour = startTime.getHour();
        this.startTimeMinute = startTime.getMinute();
        this.endTimeHour = endTime.getHour();
        this.endTimeMinute = endTime.getMinute();
        this.birdParkInfo = birdParkInfo;
        this.attraction = attraction;
    }

    public OpeningHours(String weekday, LocalTime startTime, LocalTime endTime) {
        this.weekday = weekday;
        this.startTimeHour = startTime.getHour();
        this.startTimeMinute = startTime.getMinute();
        this.endTimeHour = endTime.getHour();
        this.endTimeMinute = endTime.getMinute();
    }

    public OpeningHours() {

    }

    public Long getId() {
        return id;
    }

    public String getWeekday() {
        return weekday;
    }

    public LocalTime getStartTime() {
        return LocalTime.of(startTimeHour, startTimeMinute);
    }

    public LocalTime getEndTime() {
        return LocalTime.of(endTimeHour, endTimeMinute);
    }

    public BirdParkBasicInfo getVogelpark() {
        return birdParkInfo;
    }

    public void setVogelpark(BirdParkBasicInfo birdParkInfo) {
        this.birdParkInfo = birdParkInfo;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }
}
