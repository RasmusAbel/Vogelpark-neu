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

    @ManyToMany(
            mappedBy = "filterTags",
            cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER
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

    @Override
    public String toString() {
        return "filter tag: [" +
                "id: " + id +
                ", name: " + name +
                "]";
    }

    public Set<Attraction> getAttractions() {
        return attractions;
    }
}
