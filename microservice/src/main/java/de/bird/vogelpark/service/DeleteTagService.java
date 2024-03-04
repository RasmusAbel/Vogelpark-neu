package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeleteTagService {

    FilterTagRepository filterTagRepository;

    public DeleteTagService(FilterTagRepository filterTagRepository) {
        this.filterTagRepository = filterTagRepository;
    }

    public void deleteTag(String name){
        Optional<FilterTag> tag = filterTagRepository.findByName(name);
        tag.ifPresent(filterTag -> filterTagRepository.delete(filterTag));
    }
}
