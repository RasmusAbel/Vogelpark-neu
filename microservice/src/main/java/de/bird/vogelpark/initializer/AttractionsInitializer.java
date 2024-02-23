package de.bird.vogelpark.initializer;

import de.bird.vogelpark.BirdParkApplication;
import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import de.bird.vogelpark.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;

@Service
public class AttractionsInitializer {
    private static final Logger logger = LoggerFactory.getLogger(BirdParkApplication.class);

    private AttractionRepository attractionRepository;
    private FilterTagRepository filterTagRepository;
    private OpeningHoursRepository openingHoursRepository;
    private TagService tagService;

    public AttractionsInitializer(AttractionRepository attractionRepository,
                                  FilterTagRepository filterTagRepository,
                                  OpeningHoursRepository openingHoursRepository,
                                  TagService tagService) {
        this.attractionRepository = attractionRepository;
        this.filterTagRepository = filterTagRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.tagService = tagService;
    }

    public void initialize() {
        if(attractionRepository.count() <= 0) {
            filterTagRepository.save(new FilterTag("Natur"));
            filterTagRepository.save(new FilterTag("Familie"));

            OpeningHours aOpeningHours = new OpeningHours("Täglich", "10:00", "17:00");
            openingHoursRepository.save(aOpeningHours);
            Attraction a = new Attraction();
            a.setName("Aussichtsturm");
            a.setDescription("Gute Aussichten für Sie.");
            a.getOpeningHours().add(aOpeningHours);
            attractionRepository.save(a);

            OpeningHours bOpeningHours = new OpeningHours("Täglich", "9:00", "17:00");
            openingHoursRepository.save(bOpeningHours);
            Attraction b = new Attraction();
            b.setName("Flugkäfig");
            b.setDescription("Die können zwar fliegen, aber nicht abhauen.");
            b.getOpeningHours().add(bOpeningHours);
            attractionRepository.save(b);

            OpeningHours cOpeningHours = new OpeningHours("Täglich", "9:00", "17:00");
            openingHoursRepository.save(cOpeningHours);
            Attraction c = new Attraction();
            c.setName("Lehrpfad");
            c.setDescription("Gehen Sie bitte.");
            c.getOpeningHours().add(cOpeningHours);
            attractionRepository.save(c);
        }
    }
}
