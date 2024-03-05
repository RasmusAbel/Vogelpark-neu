package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.repositories.AttractionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteAttractionService {
    private AttractionRepository attractionRepository;

    public DeleteAttractionService(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    public ResponseEntity<String> deleteAttraction(String attractionName) {
        Optional<Attraction> foundAttraction = attractionRepository.findByName(attractionName);

        //Prüfen, ob Attraktion existiert
        if(foundAttraction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Attraction %s does not exist and can't be deleted", attractionName));
        }

        //Wenn ja, dann Attraktion löschen
        attractionRepository.delete(foundAttraction.get());
        return ResponseEntity.ok(String.format("Attraction %s successfully deleted", attractionName));
    }
}
