package de.bird.vogelpark.service.create;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.request.CreateAttractionRequest;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import de.bird.vogelpark.validator.OpeningHoursValidationResult;
import de.bird.vogelpark.validator.OpeningHoursValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class CreateAttractionService {

    private AttractionRepository attractionRepository;

    private OpeningHoursRepository openingHoursRepository;

    private FilterTagRepository filterTagRepository;

    private CreateTagService createTagService;

    private OpeningHoursValidator openingHoursValidator;

    public CreateAttractionService(AttractionRepository attractionRepository,
                                   OpeningHoursRepository openingHoursRepository,
                                   FilterTagRepository filterTagRepository,
                                   CreateTagService createTagService,
                                   OpeningHoursValidator openingHoursValidator) {
        this.attractionRepository = attractionRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.filterTagRepository = filterTagRepository;
        this.createTagService = createTagService;
        this.openingHoursValidator = openingHoursValidator;
    }

    public ResponseEntity<String> createAttraction(CreateAttractionRequest req) {
        //Wenn bereits eine Attraktion mit dem Namen exitsiert, soll keine neue erzeugt werden.
        //Stattdessen wird eine Fehlermeldung zurückgegeben.
        Optional<Attraction> foundAttraction = attractionRepository.findByName(req.name());
        if(foundAttraction.isPresent()) {
            return ResponseEntity.badRequest().body(String.format("Attraction %s already exists", req.name()));
        }

        //Öffnungszeiten müssen auf Gültigkeit geprüft werden, da später sonst Fehler auftreten.
        //Wenn die Öffnungszeiten ungültig sind, soll die Attraktion nicht erzeugt werden.
        for(CreateOpeningHoursRequest createOpHoursReq : req.openingHours()) {
            OpeningHoursValidationResult validationResult = openingHoursValidator.validate(createOpHoursReq);
            if(!openingHoursValidator.isValid(validationResult)) {
                return ResponseEntity.badRequest().body(openingHoursValidator.getValidationMessage(validationResult));
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

        return ResponseEntity.ok(String.format("Attraction %s successfully created", req.name()));
    }
}
