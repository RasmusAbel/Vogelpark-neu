package de.bird.vogelpark.service.create;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.request.CreateAttractionRequest;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import de.bird.vogelpark.utils.TimeValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class CreateAttractionService {

    private final AttractionRepository attractionRepository;

    private final OpeningHoursRepository openingHoursRepository;

    private final FilterTagRepository filterTagRepository;

    private final TimeValidator timeValidator;


    public CreateAttractionService(AttractionRepository attractionRepository,
                                   OpeningHoursRepository openingHoursRepository,
                                   FilterTagRepository filterTagRepository,
                                   TimeValidator timeValidator) {
        this.attractionRepository = attractionRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.filterTagRepository = filterTagRepository;
        this.timeValidator = timeValidator;
    }

    public ResponseEntity<String> createAttraction(CreateAttractionRequest req) {
        //Wenn bereits eine Attraktion mit dem Namen exitsiert, soll keine neue erzeugt werden.
        //Stattdessen wird eine Fehlermeldung zurückgegeben.
        Optional<Attraction> foundAttraction = attractionRepository.findByName(req.name());
        if(foundAttraction.isPresent()) {
            return ResponseEntity.badRequest().body(String.format("Attraktion '%s' existiert bereits. " +
                    "Es kann keine zweite Attraktion mit diesem Namen erzeugt werden.", req.name()));
        }

        //Öffnungszeiten müssen auf Gültigkeit geprüft werden, da später sonst Fehler auftreten.
        //Wenn die Öffnungszeiten ungültig sind, soll die Attraktion nicht erzeugt werden.
        for(CreateOpeningHoursRequest createOpHoursReq : req.openingHours()) {
            String validationResult = timeValidator.validateOpeningHours(createOpHoursReq);
            if(validationResult != null){
                return ResponseEntity.badRequest().body(validationResult);
            }
        }

        Attraction attr = new Attraction();
        attr.setName(req.name());
        attr.setDescription(req.description());
        attractionRepository.save(attr);

        //Alle Öffnungszeiten aus dem Request erzeugen und der Attraktion hinzufügen
        for(CreateOpeningHoursRequest createOpHoursReq : req.openingHours()) {
            OpeningHours openingHours = new OpeningHours(
                    createOpHoursReq.weekday(),
                    LocalTime.of(createOpHoursReq.startTimeHour(), createOpHoursReq.startTimeMinute()),
                    LocalTime.of(createOpHoursReq.endTimeHour(), createOpHoursReq.endTimeMinute()),
                    null,
                    attr
            );
            openingHoursRepository.save(openingHours);
            attr.getOpeningHours().add(openingHours);
        }

        //Für jeden der im Request gewünschten Tags:
        //Den Tag erzeuge nund der Attraktion zuordnen
        for(String tagName : req.filterTags()) {
            FilterTag newTag = new FilterTag(tagName, attr);
            filterTagRepository.save(newTag);
            attr.getFilterTags().add(newTag);
        }

        attractionRepository.save(attr);

        return ResponseEntity.ok(String.format("Attraktion '%s' wurde erzeugt.", req.name()));
    }
}
