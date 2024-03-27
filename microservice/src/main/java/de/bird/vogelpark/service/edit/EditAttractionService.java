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

import java.time.LocalTime;
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
        Optional<Attraction> foundAttraction = attractionRepository.findByName(editAttractionRequest.currentName());

        //Fehlermeldung zurückgeben, wenn versucht wird, eine Attraktion zu bearbeiten, die nicht existiert
        if(foundAttraction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(
                            "Attraction with name %s does not exist",
                            editAttractionRequest.currentName()
            ));
        }

        //Attraktion bearbeiten
        Attraction attr = foundAttraction.get();

        if(editAttractionRequest.newName() != null) {
            attr.setName(editAttractionRequest.newName());
        }

        if (editAttractionRequest.newDescription() != null) {
            attr.setDescription(editAttractionRequest.newDescription());
        }

        if(editAttractionRequest.newImageUrl() != null) {
            attr.setImageUrl(editAttractionRequest.newImageUrl());
        }

        removeOpeningHours(attr, editAttractionRequest.openingHourIdsToRemove());
        addOpeningHours(attr, editAttractionRequest.openingHoursToAdd());
        removeFilterTags(attr, editAttractionRequest.filterTagsToRemove());
        addFilterTags(attr, editAttractionRequest.filterTagsToAdd());

        attractionRepository.save(attr);

        return ResponseEntity.ok(String.format(
                "Attraction %s successfully edited",
                editAttractionRequest.currentName()
        ));
    }

    /**
     * Translate the upper comment to english
     * Adds the passed opening hours to an attraction
     * @param attr The attraction to which the opening hours should be added
     * @param opHoursToAdd The opening hours to be added
     */
    private void addOpeningHours(Attraction attr, CreateOpeningHoursRequest[] opHoursToAdd) {
        for(CreateOpeningHoursRequest createOpHoursReq : opHoursToAdd) {
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
    }

    /**
     * Removes the opening hours with the passed ids from an attraction
     * @param attr The attraction from which the opening hours should be removed
     * @param opHourIdsToRemove The IDs of the opening hours to be removed
     */
    private void removeOpeningHours(Attraction attr, Long[] opHourIdsToRemove) {
        for(Long opHoursId : opHourIdsToRemove) {
            Optional<OpeningHours> foundOpHours = openingHoursRepository.findById(opHoursId);
            if(foundOpHours.isPresent()) {
                OpeningHours openingHours = foundOpHours.get();
                attr.getOpeningHours().remove(openingHours);
                openingHoursRepository.delete(openingHours);
            }
        }
    }

    /**
     * Adds the passed filter tags to an attraction
     * @param attr The attraction to which the filter tags should be added
     * @param tagsToAdd The names of the filter tags to be added
     */
    private void addFilterTags(Attraction attr, String[] tagsToAdd) {
        for(String tagNameToAdd : tagsToAdd) {

            //Tags should only be added if they don't already exist in the attraction
            boolean tagAlreadyExists = false;
            for(FilterTag existingTag : attr.getFilterTags()) {
                if(existingTag.getName().equals(tagNameToAdd)) {
                    tagAlreadyExists = true;
                    break;
                }
            }

            //Add the tag if it doesn't already exist
            if(!tagAlreadyExists) {
                FilterTag newTag = new FilterTag(tagNameToAdd, attr);
                filterTagRepository.save(newTag);
                attr.getFilterTags().add(newTag);
                attractionRepository.save(attr);
            }
        }
    }

    /**
     * Removes the filter tags with the passed names from an attraction
     * @param attr The attraction from which the filter tags should be removed
     * @param tagsToRemove The names of the filter tags to be removed
     */
    private void removeFilterTags(Attraction attr, String[] tagsToRemove) {
        for(String tagNameToRemove : tagsToRemove) {
            attr.getFilterTags().removeIf(tag -> tag.getName().equals(tagNameToRemove));
            attractionRepository.save(attr);

            //Optional<FilterTag> foundTag = filterTagRepository.findByName(tagNameToRemove);
            //foundTag.ifPresent(filterTag -> filterTagRepository.delete(filterTag));
        }
    }
}
