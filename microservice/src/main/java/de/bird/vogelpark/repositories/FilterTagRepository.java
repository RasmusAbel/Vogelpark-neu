package de.bird.vogelpark.repositories;

import de.bird.vogelpark.beans.FilterTag;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FilterTagRepository extends CrudRepository<FilterTag, Long> {
    public Optional<FilterTag> findByName(String name);
}
