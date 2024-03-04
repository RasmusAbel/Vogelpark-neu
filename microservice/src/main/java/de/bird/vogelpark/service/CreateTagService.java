package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.springframework.stereotype.Service;


@Service
public class CreateTagService {

    private FilterTagRepository filterTagRepository;


    public CreateTagService(FilterTagRepository filterTagRepository) {
        this.filterTagRepository = filterTagRepository;
    }

    public void createTag(String tagName){
        FilterTag tag = new FilterTag();
        tag.setName(tagName);
        filterTagRepository.save(tag);
    }
}
