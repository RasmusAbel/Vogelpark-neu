package de.bird.vogelpark.service.edit;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.dto.request.EditTourRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.TourRepository;
import de.bird.vogelpark.utils.TimeValidationResult;
import de.bird.vogelpark.utils.TimeValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class EditTourService {
    private final TourRepository tourRepository;
    private final TimeValidator timeValidator;
    private final AttractionRepository attractionRepository;

    public EditTourService(TourRepository tourRepository,
                           TimeValidator timeValidator,
                           AttractionRepository attractionRepository) {
        this.tourRepository = tourRepository;
        this.timeValidator = timeValidator;
        this.attractionRepository = attractionRepository;
    }

    public ResponseEntity<String> editTour(EditTourRequest req) {
        Optional<Tour> foundTour = tourRepository.findByName(req.currentName());

        if(foundTour.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(
                    "Tour with name %s does not exist",
                    req.currentName()
            ));
        }

        Tour tour = foundTour.get();

        if(req.newName() != null) {
            tour.setName(req.newName());
        }
        if (req.newDescription() != null) {
            tour.setDescription(req.newDescription());
        }
        if (req.newPriceCents() != null) {
            tour.setPriceCents(req.newPriceCents());
        }
        if (req.newImageUrl() != null) {
            tour.setImageUrl(req.newImageUrl());
        }

        ResponseEntity<String> setTimesResponse = setTimes(
                tour,
                req.newStartTimeHour(), req.newStartTimeMinute(),
                req.newEndTimeHour(), req.newEndTimeMinute()
        );
        if(setTimesResponse != null) {
            return setTimesResponse;
        }

        ResponseEntity<String> addAttractionsResponse = addAttractions(tour, req.attractionNamesToAdd());
        if(addAttractionsResponse != null) {
            return addAttractionsResponse;
        }
        removeAttractions(tour, req.attractionNamesToRemove());

        tourRepository.save(tour);

        return ResponseEntity.ok(String.format(
                "Tour %s successfully edited",
                req.currentName()
        ));
    }

    /**
     * Setzt die Start- und Endzeit einer Tour, wenn die neuen Werte gültig sind.
     * Wenn die Werte gültig sind, wird null zurückgegeben, sonst eine Fehlermeldung.
     * @param tour
     * @param startHour
     * @param startMinute
     * @param endHour
     * @param endMinute
     * @return null, wenn die Werte gültig sind, sonst eine ResponseEntity mit der
     * entsprechenden Fehlermeldung
     */
    private ResponseEntity<String> setTimes(
            Tour tour,
            Integer startHour,
            Integer startMinute,
            Integer endHour,
            Integer endMinute
    ) {
        //Variablen werden später genutzt, um zu prüfen, ob die (ggf. neue) Startzeit
        //vor der (ggf. neuen) Endzeit liegt
        LocalTime startTime = tour.getStartTime();
        LocalTime endTime = tour.getEndTime();



        //Wenn die Startzeit geändert werden soll, wird die neue Startzeit validiert
        if(startHour != null && startMinute != null) {
            TimeValidationResult startTimeValidationResult = timeValidator.validateValueRange(
                    startHour, startMinute
            );

            //Fehlermeldung an den Client geben, wenn die neue Startzeit ungültig ist
            if(!timeValidator.isValid(startTimeValidationResult)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        timeValidator.getValidationMessage(startTimeValidationResult)
                );
            }
            startTime = LocalTime.of(startHour, startMinute);
        }



        //Wenn die Endzeit geändert werden soll, wird die neue Endzeit validiert
        if(endHour != null && endMinute != null) {
            TimeValidationResult endTimeValidationResult = timeValidator.validateValueRange(
                    endHour, endMinute
            );

            //Fehlermeldung an den Client geben, wenn die neue Endzeit ungültig ist
            if(!timeValidator.isValid(endTimeValidationResult)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        timeValidator.getValidationMessage(endTimeValidationResult)
                );
            }
            endTime = LocalTime.of(endHour, endMinute);
        }

        //Wenn startTime nach endTime liegt, wird die entsprechende Fehlermeldung zurückgegeben
        TimeValidationResult validationResult = timeValidator.validateStartBeforeEnd(startTime, endTime);
        if(!timeValidator.isValid(validationResult)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    timeValidator.getValidationMessage(validationResult)
            );
        }

        //Wenn alle Prüfungen oben bestanden wurden, werden die neuen Start- und Endzeiten
        //der Tour zugewiesen
        tour.setStartTime(startTime);
        tour.setEndTime(endTime);

        return null;
    }

    /**
     * Fügt der Tour die Attraktionen hinzu, die in namesToAdd angegeben sind.
     * Gibt eine Fehlermeldung zurück, wenn keine Attraktion zu einem der Namen existiert.
     * @param tour
     * @param namesToAdd
     * @return null, wenn alle Attraktionen hinzugefügt werden konnten, sonst eine
     * ResponseEntity mit der entsprechenden Fehlermeldung
     */
    private ResponseEntity<String> addAttractions(Tour tour, String[] namesToAdd) {
        //Für jede der Attraktionen, die der Tour hinzugefügt werden sollen
        //prüfen, ob diese existieren und ggf. hinzufügen.
        for(String name : namesToAdd) {
            Optional<Attraction> foundAttraction = attractionRepository.findByName(name);

            //In der Tour sollen keine Attraktionen referenziert werden, die nicht existieren.
            //Wenn also keine passende Attraktion zu den angegebenen Namen existiert,
            //wird eine Fehlermeldung an den Client zurückgegeben
            if(foundAttraction.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(
                        "Attraction with name %s does not exist and therefore" +
                                " cannot be added to tour %s",
                        name, tour.getName()
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

        return null;
    }

    /**
     * Entfernt die Attraktionen, die in namesToRemove angegeben sind, aus der Tour.
     * Wenn die entsprechenden Attraktionen nicht in der Tour referenziert sind,
     * passiert nichts.
     * @param tour
     * @param namesToRemove
     */
    private void removeAttractions(Tour tour, String[] namesToRemove) {
        for(String attrName : namesToRemove) {
            Optional<Attraction> foundAttraction = attractionRepository.findByName(attrName);
            if(foundAttraction.isPresent()) {
                Attraction a = foundAttraction.get();
                tour.getAttractions().remove(a);
                a.getTours().remove(tour);
                attractionRepository.save(a);
                tourRepository.save(tour);
            }
        }
    }
}
