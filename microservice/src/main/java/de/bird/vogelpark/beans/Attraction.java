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
    private List<OpeningHours> openingHours = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(name = "attraction_tags", joinColumns = @JoinColumn(name = "attraction_id"), inverseJoinColumns = @JoinColumn(name = "filter_tag_id"))
    private Set<FilterTag> filterTags = new HashSet<>();

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

    public List<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public Set<FilterTag> getFilterTags() {
        return filterTags;
    }

    @Override
    public String toString() {
        return "attraction: [" +
                "id: " + id +
                ", name: " + name +
                ", description: " + description +
                "]";
    }
}
