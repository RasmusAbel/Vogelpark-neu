package de.bird.vogelpark.service.delete;

import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class DeleteTagService {

    FilterTagRepository filterTagRepository;

    public DeleteTagService(FilterTagRepository filterTagRepository) {
        this.filterTagRepository = filterTagRepository;
    }

    public ResponseEntity<String> deleteTag(String tagName) {
        List<FilterTag> foundTags = filterTagRepository.findAllByName(tagName);

        if (foundTags.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("Tag %s is never used and therefore can't be deleted", tagName));
        }

        /*
        for (FilterTag tag : foundTags) {
            if(tag.getAttraction() != null) {
                tag.getAttraction().getFilterTags().remove(tag);
            }
            filterTagRepository.delete(tag);
        }

         */

        return ResponseEntity.ok(String.format("All occurrences of Tag %s successfully deleted", tagName));
    }
}
