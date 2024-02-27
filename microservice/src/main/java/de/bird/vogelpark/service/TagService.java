package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Filter;

@Service
public class TagService {
    private FilterTagRepository filterTagRepository;

    public TagService(FilterTagRepository filterTagRepository) {
        this.filterTagRepository = filterTagRepository;
    }

    /**
     * Gibt das FilterTag-Objekt zu dem tagName zurück. Wenn
     * noch kein passender FilterTag in der DB existiert, wird
     * ein neuer erzeugt und zurückgegeben.
     * @param tagName
     * @return Der gefundene oder erzeugte FilterTag
     **/
    public FilterTag findOrCreateNewTag(String tagName) {
        //return filterTagRepository.findByName(tagName)
        //        .orElseGet(() -> filterTagRepository.save(new FilterTag(tagName)));

        Optional<FilterTag> foundTag = filterTagRepository.findByName(tagName);

        if(foundTag.isPresent()) {
            return foundTag.get();
        }

        FilterTag newTag = new FilterTag();
        newTag.setName(tagName);
        return newTag;
    }

    public List<String> getAllTags() {
        List<FilterTag> allTags = new ArrayList<>();

        for(FilterTag tag : filterTagRepository.findAll()) {
            allTags.add(tag);
        }

        return mapData2Response(allTags);
    }

    /**
     * Prüft, ob in der DB ein FilterTag zu dem angegebenen
     * tagName existiert.
     * @param tagName
     * @return True, wenn ein entsprechender FilterTag
     * existiert, sonst false.
     */
    public boolean existsTag(String tagName) {
        return filterTagRepository.findByName(tagName).isPresent();
    }

    public List<String> mapData2Response(List<FilterTag> data) {
        List<String> response = new ArrayList<>();
        for(FilterTag tag : data) {
            response.add(tag.getName());
        }
        return response;
    }
}
