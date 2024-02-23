package de.bird.vogelpark.repositories;

import de.bird.vogelpark.beans.Attraction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttractionRepository extends CrudRepository <Attraction, Long> {
    @Query("SELECT a FROM Attraction a JOIN a.filterTags t WHERE t.name IN :tagNames GROUP BY a HAVING COUNT(DISTINCT t) = :tagCount")
    List<Attraction> findAttractionsByTags(@Param("tagNames") List<String> tagNames, @Param("tagCount") int tagCount);
}
