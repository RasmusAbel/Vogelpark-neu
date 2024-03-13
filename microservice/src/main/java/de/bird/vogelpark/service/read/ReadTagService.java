package de.bird.vogelpark.service.read;

import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReadTagService {
    private FilterTagRepository filterTagRepository;

    public ReadTagService(FilterTagRepository filterTagRepository) {
        this.filterTagRepository = filterTagRepository;
    }

    public List<String> readAllTags() {
        Set<String> uniqueTags = new HashSet<>();

        for(FilterTag tag : filterTagRepository.findAll()) {
            uniqueTags.add(tag.getName());
        }

        return new ArrayList<>(uniqueTags);
    }
}
