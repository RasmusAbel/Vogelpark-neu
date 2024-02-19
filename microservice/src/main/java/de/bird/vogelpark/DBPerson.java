package de.bird.vogelpark;

import jakarta.persistence.*;

@Entity
public class DBPerson {
    @Id @GeneratedValue(strategy=GenerationType.AUTO) Long id;

    public DBPerson() {}

    public Long getId() {return this.id;};
}
