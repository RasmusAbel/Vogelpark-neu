package de.bird.vogelpark.repositories;

import de.bird.vogelpark.beans.Tour;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TourRepository extends CrudRepository<Tour, Long> {
    Optional<Tour> findByName(String name);
}
