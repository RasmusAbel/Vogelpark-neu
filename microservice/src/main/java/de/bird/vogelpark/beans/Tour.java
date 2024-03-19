package de.bird.vogelpark.beans;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private int priceCents;
    @Column
    private String imageUrl;
    @Column
    private int startTimeHour, startTimeMinute;
    @Column
    private int endTimeHour, endTimeMinute;
    @Column
    private long durationSeconds;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinTable(
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "attraction_id")
    )
    private Set<Attraction> attractions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(int price) {
        this.priceCents = price;
    }

    public Set<Attraction> getAttractions() {
        return attractions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStartTime(LocalTime startTime) {
        LocalTime endTime = LocalTime.of(endTimeHour, endTimeMinute);
        this.startTimeHour = startTime.getHour();
        this.startTimeMinute = startTime.getMinute();
        this.durationSeconds = Duration.between(startTime, endTime).getSeconds();
    }

    public void setEndTime(LocalTime endTime) {
        LocalTime startTime = LocalTime.of(startTimeHour, startTimeMinute);
        this.endTimeHour = endTime.getHour();
        this.endTimeMinute = endTime.getMinute();
        this.durationSeconds = Duration.between(startTime, endTime).getSeconds();
    }

    public LocalTime getStartTime() {
        return LocalTime.of(startTimeHour, startTimeMinute);
    }

    public LocalTime getEndTime() {
        return LocalTime.of(endTimeHour, endTimeMinute);
    }

    public String getDurationString() {
        long hours = Duration.ofSeconds(durationSeconds).toHoursPart();
        long minutes = Duration.ofSeconds(durationSeconds).toMinutesPart();

        return hours + ":" + minutes + (minutes < 10 ? "0" : "");
    }
}
