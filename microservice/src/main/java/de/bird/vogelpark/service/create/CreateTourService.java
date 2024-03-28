package de.bird.vogelpark.service.create;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.dto.request.CreateTourRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.TourRepository;
import de.bird.vogelpark.utils.TimeValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class CreateTourService {

    private final TourRepository tourRepository;

    private final AttractionRepository attractionRepository;

    private final TimeValidator timeValidator;

    public CreateTourService(TourRepository tourRepository, AttractionRepository attractionRepository, TimeValidator timeValidator) {
        this.tourRepository = tourRepository;
        this.attractionRepository = attractionRepository;
        this.timeValidator = timeValidator;
    }

    public ResponseEntity<String> createTour(CreateTourRequest request) {
        //Wenn bereits eine Tour mit dem Namen exitsiert, soll keine neue erzeugt werden.
        //Stattdessen wird eine Fehlermeldung zurückgegeben.
        Optional<Tour> foundTour = tourRepository.findByName(request.name());
        if (foundTour.isPresent()) {
            return ResponseEntity.badRequest().body(String.format("Tour '%s' existiert bereits. " +
                    "Es kann keine zweite Tour mit diesem Namen erzeugt werden.", request.name()));
        }

        //Start- und Endzeit müssen auf Gültigkeit geprüft werden, da später sonst Fehler auftreten.
        //Wenn die Zeiten ungültig sind, soll die Tour nicht erzeugt werden.
        String validationResult = timeValidator.validateTimeInput(request.startTimeHour(), request.startTimeMinute(), request.endTimeHour(), request.endTimeMinute());
        if(validationResult != null){
            return ResponseEntity.badRequest().body(validationResult);
        }

        Tour tour = new Tour();
        tour.setName(request.name());
        tour.setPriceCents(request.priceCents());
        tour.setImageUrl(request.imageUrl());
        tour.setDescription(request.description());
        tour.setStartTime(LocalTime.of(request.startTimeHour(), request.startTimeMinute()));
        tour.setEndTime(LocalTime.of(request.endTimeHour(), request.endTimeMinute()));
        tourRepository.save(tour);

        //Für jede der Attraktionen, die der Tour hinzugefügt werden sollen
        //prüfen, ob diese existieren und ggf. hinzufügen.
        for (String attractionName : request.attractionNames()) {
            Optional<Attraction> foundAttraction = attractionRepository.findByName(attractionName);

            //In der Tour sollen keine Attraktionen referenziert werden, die nicht existieren.
            //Wenn also keine passende Attraktion zu den angegebenen Namen existiert,
            //wird eine Fehlermeldung an den Client zurückgegeben
            if(foundAttraction.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(
                        "Attraktion '%s' existiert nicht und kann der Tour '%s' " +
                                "daher nicht hinzugefügt werden.",
                        attractionName, tour.getName()
                ));
            }

            //Wenn die Attraktion existiert, wird sie in der Tour
            //referenziert und andersrum
            Attraction a = foundAttraction.get();
            tour.getAttractions().add(a);
            a.getTours().add(tour);
            attractionRepository.save(a);
            tourRepository.save(tour);
        }

        return ResponseEntity.ok(String.format("Tour '%s' wurde erzeugt.", request.name()));
    }

}
