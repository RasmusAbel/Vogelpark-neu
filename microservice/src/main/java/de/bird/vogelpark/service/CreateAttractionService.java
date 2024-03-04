package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreateAttractionService {

    private AttractionRepository attractionRepository;

    private OpeningHoursRepository openingHoursRepository;

    private FilterTagRepository filterTagRepository;

    public CreateAttractionService(AttractionRepository attractionRepository, OpeningHoursRepository openingHoursRepository, FilterTagRepository filterTagRepository) {
        this.attractionRepository = attractionRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.filterTagRepository = filterTagRepository;
    }

    public void createAttraction(List<String> data) {

        Attraction attraction = new Attraction();
        attraction.setName(data.get(0));
        attraction.setDescription(data.get(1));

        OpeningHours openingHours = new OpeningHours(data.get(2), data.get(3), data.get(4), null, attraction);
        attraction.getOpeningHours().add(openingHours);

        for (String tagName : data.subList(5, data.size())) {
            Optional<FilterTag> tag = filterTagRepository.findByName(tagName);
            tag.ifPresent(filterTag -> attraction.getFilterTags().add(filterTag));
        }

        attractionRepository.save(attraction);
        openingHoursRepository.save(attraction.getOpeningHours().get(0));
    }
}
