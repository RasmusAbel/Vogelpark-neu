package de.bird.vogelpark.initializer;

import de.bird.vogelpark.BirdParkApplication;
import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import de.bird.vogelpark.service.read.ReadTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AttractionsInitializer {
    private static final Logger logger = LoggerFactory.getLogger(BirdParkApplication.class);

    private AttractionRepository attractionRepository;
    private FilterTagRepository filterTagRepository;
    private OpeningHoursRepository openingHoursRepository;
    private ReadTagService findTagService;

    public AttractionsInitializer(AttractionRepository attractionRepository,
                                  FilterTagRepository filterTagRepository,
                                  OpeningHoursRepository openingHoursRepository,
                                  ReadTagService findTagService) {
        this.attractionRepository = attractionRepository;
        this.filterTagRepository = filterTagRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.findTagService = findTagService;
    }

    public void initialize() {
        if(attractionRepository.count() < 1) {

            Attraction a = new Attraction();
            a.setName("Aussichtsturm");
            a.setDescription("Gute Aussichten für Sie.");

            OpeningHours aOpeningHours = new OpeningHours("Täglich", "10:00", "17:00", null, a);
            a.getOpeningHours().add(aOpeningHours);

            openingHoursRepository.save(aOpeningHours);
            attractionRepository.save(a);



            Attraction b = new Attraction();
            b.setName("Flugkäfig");
            b.setDescription("Die können zwar fliegen, aber nicht abhauen.");

            OpeningHours bOpeningHours = new OpeningHours("Täglich", "9:00", "17:00", null, b);
            //bOpeningHours.setAttraction(b);
            b.getOpeningHours().add(bOpeningHours);

            openingHoursRepository.save(bOpeningHours);
            attractionRepository.save(b);



            Attraction c = new Attraction();
            c.setName("Lehrpfad");
            c.setDescription("Gehen Sie bitte.");

            OpeningHours cOpeningHours = new OpeningHours("Täglich", "9:00", "17:00", null, c);
            c.getOpeningHours().add(cOpeningHours);

            FilterTag naturTag = new FilterTag("Natur", c);
            c.getFilterTags().add(naturTag);

            attractionRepository.save(c);
            filterTagRepository.save(naturTag);
            openingHoursRepository.save(cOpeningHours);

            for(String tag : findTagService.readAllTags()) {
                logger.info("Neuer Tag hinzugefügt: " + tag);
            }
        }
    }
}
