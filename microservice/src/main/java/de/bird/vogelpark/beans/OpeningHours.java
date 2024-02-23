package de.bird.vogelpark.beans;

import jakarta.persistence.*;

@Entity
@Table
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String weekday;

    @Column
    private String startTime;

    @Column
    private String endTime;

    @ManyToOne(optional = true)
    @JoinColumn(name = "bird_park_id")
    //Fremdschlüssel auf den Vogelpark, dessen Öffnungszeiten angegeben werden
    private BirdParkBasicInfo birdParkInfo;

    @ManyToOne(optional = true)
    @JoinColumn(name = "attraction_id")
    //ODER Fremdschlüssel auf die Attraktion, deren Öffnungszeiten angegeben werden
    private Attraction attraction;

    public OpeningHours(String weekday, String startTime, String endTime, BirdParkBasicInfo birdParkInfo, Attraction attraction) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
        this.birdParkInfo = birdParkInfo;
        this.attraction = attraction;
    }

    public OpeningHours(String weekday, String startTime, String endTime) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public OpeningHours() {

    }

    public Long getId() {
        return id;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    @Override
    public String toString() {
        String output = "opening hours: [" +
                "id: " + id +
                ", weekday: " + weekday +
                ", start time: " + startTime +
                ", end time: " + endTime;

        if(birdParkInfo != null) {
            output += ", bird_park_id: " + birdParkInfo.getId();
        }

        if(attraction != null) {
            output += ", attraction_id: " + attraction.getId();
        }

        return output + "]";
    }
}
