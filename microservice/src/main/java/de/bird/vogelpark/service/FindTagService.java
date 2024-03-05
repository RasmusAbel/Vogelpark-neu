package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FindTagService {
    private FilterTagRepository filterTagRepository;

    public FindTagService(FilterTagRepository filterTagRepository) {
        this.filterTagRepository = filterTagRepository;
    }

    public List<String> getAllTags() {
        Set<String> uniqueTags = new HashSet<>();

        for(FilterTag tag : filterTagRepository.findAll()) {
            uniqueTags.add(tag.getName());
        }

        return new ArrayList<>(uniqueTags);
    }
}
