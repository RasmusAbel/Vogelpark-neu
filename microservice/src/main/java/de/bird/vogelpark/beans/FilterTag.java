package de.bird.vogelpark.beans;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class FilterTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne(optional = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "attraction_id")
    private Attraction attraction;

    public FilterTag() {
    }

    public FilterTag(String name, Attraction attraction) {
        this.name = name;
        this.attraction = attraction;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "filter tag: [" +
                "id: " + id +
                ", name: " + name +
                "]";
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }
}
