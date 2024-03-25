package de.bird.vogelpark.service.delete;

import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.repositories.TourRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteTourService {

    private final TourRepository tourRepository;


    public DeleteTourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public ResponseEntity<String> deleteTour(String tourName){
        Optional<Tour> foundTour = tourRepository.findByName(tourName);

        //Prüfen, ob Attraktion existiert
        if(foundTour.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Tour %s does not exist and can't be deleted", tourName));
        }

        //Wenn ja, dann Attraktion löschen
        tourRepository.delete(foundTour.get());
        return ResponseEntity.ok(String.format("Tour %s successfully deleted", tourName));
    }
}
