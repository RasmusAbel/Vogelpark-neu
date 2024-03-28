package de.bird.vogelpark.service.create;

import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class CreateTagService {

    private FilterTagRepository filterTagRepository;

    public CreateTagService(FilterTagRepository filterTagRepository) {
        this.filterTagRepository = filterTagRepository;
    }

    /**
     * Erzeugt einen neuen FilterTag mit dem übergebenen Namen.
     * Wenn bereits ein Tag mit dem Namen existiert, wird kein neuer
     * erzeugt.
     * @param tagName Name des neuen Tags
     * @return ResponseEntity mit entsprechendem Status, je nachdem, ob
     * ein Tag erzeugt wurde oder nicht
     */
    public ResponseEntity<String> createTag(String tagName) {
        //Prüfen, ob bereits ein FilterTag mit dem Namen existiert
        if(filterTagRepository.findByName(tagName).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Tag '%s' existiert bereits.", tagName));
        }

        //Ansonsten neuen Tag erzeugen
        FilterTag tag = new FilterTag();
        tag.setName(tagName);
        filterTagRepository.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("Tag '%s' wurde erzeugt.", tagName));
    }
}
