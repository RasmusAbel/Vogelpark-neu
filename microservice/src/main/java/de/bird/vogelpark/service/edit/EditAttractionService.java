package de.bird.vogelpark.service.edit;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.dto.request.EditAttractionRequest;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditAttractionService {
    private AttractionRepository attractionRepository;
    private OpeningHoursRepository openingHoursRepository;
    private FilterTagRepository filterTagRepository;

    public EditAttractionService(AttractionRepository attractionRepository,
                                 OpeningHoursRepository openingHoursRepository,
                                 FilterTagRepository filterTagRepository) {
        this.attractionRepository = attractionRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.filterTagRepository = filterTagRepository;
    }

    public ResponseEntity<String> editAttraction(EditAttractionRequest editAttractionRequest) {
        Optional<Attraction> foundAttraction = attractionRepository.findByName(editAttractionRequest.getCurrentName());

        //Fehlermeldung zurückgeben, wenn versucht wird, eine Attraktion zu bearbeiten, die nicht existiert
        if(foundAttraction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(
                            "Attraction with name %s does not exist",
                            editAttractionRequest.getCurrentName()
            ));
        }

        //Attraktion bearbeiten
        Attraction attr = foundAttraction.get();

        if(editAttractionRequest.getNewName() != null) {
            attr.setName(editAttractionRequest.getNewName());
        }

        if (editAttractionRequest.getNewDescription() != null) {
            attr.setDescription(editAttractionRequest.getNewDescription());
        }

        //Neue Öffnungszeiten hinzufügen
        for(CreateOpeningHoursRequest createOpHoursReq : editAttractionRequest.getOpeningHoursToAdd()) {
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

        //Zu entfernende Öffnungszeiten entfernen
        for(Long opHoursId : editAttractionRequest.getOpeningHourIdsToRemove()) {
            Optional<OpeningHours> foundOpHours = openingHoursRepository.findById(opHoursId);
            if(foundOpHours.isPresent()) {
                OpeningHours openingHours = foundOpHours.get();
                attr.getOpeningHours().remove(openingHours);
                openingHoursRepository.delete(openingHours);
            }
        }

        //Neue FilterTags hinzufügen
        for(String tagNameToAdd : editAttractionRequest.getFilterTagsToAdd()) {
            FilterTag newTag = new FilterTag(tagNameToAdd, attr);
            filterTagRepository.save(newTag);
            attr.getFilterTags().add(newTag);
        }

        //Zu entfernende FilterTags entfernen
        for(String tagNameToRemove : editAttractionRequest.getFilterTagsToRemove()) {
            attr.getFilterTags().removeIf(tag -> tag.getName().equals(tagNameToRemove));

            Optional<FilterTag> foundTag = filterTagRepository.findByName(tagNameToRemove);
            foundTag.ifPresent(filterTag -> filterTagRepository.delete(filterTag));
        }

        return ResponseEntity.ok(String.format(
                "Attraction %s successfully edited",
                editAttractionRequest.getCurrentName()
        ));
    }
}
