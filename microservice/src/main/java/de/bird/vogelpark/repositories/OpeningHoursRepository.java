package de.bird.vogelpark.repositories;

import de.bird.vogelpark.beans.OpeningHours;
import org.springframework.data.repository.CrudRepository;

public interface OpeningHoursRepository extends CrudRepository <OpeningHours, Long> {
}
