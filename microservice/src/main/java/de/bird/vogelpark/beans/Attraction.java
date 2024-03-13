package de.bird.vogelpark.beans;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private Set<OpeningHours> openingHours = new HashSet<>();

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FilterTag> filterTags = new HashSet<>();

    @ManyToMany(
            fetch = FetchType.EAGER,
            mappedBy = "attractions",
            cascade = CascadeType.MERGE
    )
    private Set<Tour> tours = new HashSet<>();

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

    public Set<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public Set<FilterTag> getFilterTags() {
        return filterTags;
    }

    public Set<Tour> getTours() {
        return tours;
    }

    @Override
    public String toString() {
        return "attraction: [id: " + id +
                ", name: " + name +
                ", description: " + description +
                ", number of opening hours: " + openingHours.size() +
                ", number of tags: " + filterTags.size() +
                "]";
    }
}
