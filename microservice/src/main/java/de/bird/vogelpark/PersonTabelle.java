package de.bird.vogelpark;

import org.springframework.data.repository.CrudRepository;

public interface PersonTabelle extends CrudRepository<DBPerson, Long> {
}
