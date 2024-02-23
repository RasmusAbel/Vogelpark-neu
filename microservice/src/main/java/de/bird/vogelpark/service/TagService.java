package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
     * ein neuer in der DB gespeichert und zurückgegeben.
     * @param tagName
     * @return Der gefundene oder erzeugte FilterTag
     **/
    public FilterTag findOrSaveNewTag(String tagName) {
        return filterTagRepository.findByName(tagName)
                .orElseGet(() -> filterTagRepository.save(new FilterTag(tagName)));
    }

    public List<FilterTag> getAllTags() {
        List<FilterTag> allTags = new ArrayList<>();

        for(FilterTag tag : filterTagRepository.findAll()) {
            allTags.add(tag);
        }

        return allTags;
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
}
