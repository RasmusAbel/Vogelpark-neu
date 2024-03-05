package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.request.CreateAttractionRequest;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateAttractionService {

    private AttractionRepository attractionRepository;

    private OpeningHoursRepository openingHoursRepository;

    private FilterTagRepository filterTagRepository;

    private CreateTagService createTagService;

    public CreateAttractionService(AttractionRepository attractionRepository,
                                   OpeningHoursRepository openingHoursRepository,
                                   FilterTagRepository filterTagRepository,
                                   CreateTagService createTagService) {
        this.attractionRepository = attractionRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.filterTagRepository = filterTagRepository;
        this.createTagService = createTagService;
    }

    public ResponseEntity<String> createAttraction(CreateAttractionRequest req) {
        //Wenn bereits eine Attraktion mit dem Namen exitsiert, soll keine neue erzeugt werden.
        //Stattdessen wird eine Fehlermeldung zurückgegeben.
        Optional<Attraction> foundAttraction = attractionRepository.findByName(req.getName());
        if(foundAttraction.isPresent()) {
            return ResponseEntity.badRequest().body(String.format("Attraction %s already exists", req.getName()));
        }

        Attraction attr = new Attraction();
        attr.setName(req.getName());
        attr.setDescription(req.getDescription());

        //Alle Öffnungszeiten aus dem Request erzeugen und der Attraktion hinzufügen
        for(CreateOpeningHoursRequest createOpHoursReq : req.getOpeningHours()) {
            OpeningHours openingHours = new OpeningHours(
                    createOpHoursReq.getWeekday(),
                    createOpHoursReq.getStartTime(),
                    createOpHoursReq.getEndTime(),
                    null,
                    attr
            );
            openingHoursRepository.save(openingHours);
            attr.getOpeningHours().add(openingHours);
        }

        //Für jeden der im Request gewünschten Tags:
        //Den Tag erzeuge nund der Attraktion zuordnen
        for(String tagName : req.getFilterTags()) {
            FilterTag newTag = new FilterTag(tagName, attr);
            filterTagRepository.save(newTag);
            attr.getFilterTags().add(newTag);
        }

        return ResponseEntity.ok(String.format("Attraction %s successfully created", req.getName()));
    }
}
