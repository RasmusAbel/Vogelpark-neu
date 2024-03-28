package de.bird.vogelpark.service.delete;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.TourRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteAttractionService {
    private AttractionRepository attractionRepository;
    private TourRepository tourRepository;

    public DeleteAttractionService(AttractionRepository attractionRepository,
                                   TourRepository tourRepository) {
        this.attractionRepository = attractionRepository;
        this.tourRepository = tourRepository;
    }

    public ResponseEntity<String> deleteAttraction(String attractionName) {
        Optional<Attraction> foundAttraction = attractionRepository.findByName(attractionName);

        //Prüfen, ob Attraktion existiert
        if(foundAttraction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Attraction %s does not exist and can't be deleted", attractionName));
        }

        //Attraktion aus allen entsprechenden Touren entfernen,
        //damit keine Referenzen auf die zu löschende Attraktion mehr existieren
        for(Tour t : foundAttraction.get().getTours()) {
            t.getAttractions().remove(foundAttraction.get());
            tourRepository.save(t);
        }

        //Wenn ja, dann Attraktion löschen
        attractionRepository.delete(foundAttraction.get());
        return ResponseEntity.ok(String.format("Attraction %s successfully deleted", attractionName));
    }
}
