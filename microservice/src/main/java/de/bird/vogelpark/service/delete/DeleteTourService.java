package de.bird.vogelpark.service.delete;

import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.TourRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteTourService {

    private final TourRepository tourRepository;
    private final AttractionRepository attractionRepository;

    public DeleteTourService(TourRepository tourRepository,
                             AttractionRepository attractionRepository) {
        this.tourRepository = tourRepository;
        this.attractionRepository = attractionRepository;
    }

    public ResponseEntity<String> deleteTour(String tourName){
        Optional<Tour> foundTour = tourRepository.findByName(tourName);

        //Prüfen, ob Attraktion existiert
        if(foundTour.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Tour %s does not exist and can't be deleted", tourName));
        }

        //Tour aus allen beteiligten Attraktionen entfernen,
        //damit keine Referenzen auf die zu löschende Tour mehr existieren
        for(Attraction a : foundTour.get().getAttractions()) {
            a.getTours().remove(foundTour.get());
            attractionRepository.save(a);
        }

        //Wenn ja, dann Attraktion löschen
        tourRepository.delete(foundTour.get());
        return ResponseEntity.ok(String.format("Tour %s successfully deleted", tourName));
    }
}
