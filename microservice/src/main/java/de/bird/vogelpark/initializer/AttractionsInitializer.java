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

import java.time.LocalTime;

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

            OpeningHours aOpeningHours = new OpeningHours(
                    "Täglich",
                    LocalTime.of(10, 0),
                    LocalTime.of(17, 0)
            );
            //openingHoursRepository.save(aOpeningHours);

            a.getOpeningHours().add(aOpeningHours);
            attractionRepository.save(a);

            aOpeningHours.setAttraction(a);
            openingHoursRepository.save(aOpeningHours);



            Attraction b = new Attraction();
            b.setName("Flugkäfig");
            b.setDescription("Die können zwar fliegen, aber nicht abhauen.");

            OpeningHours bOpeningHours = new OpeningHours(
                    "Täglich",
                    LocalTime.of(9, 0),
                    LocalTime.of(17, 0)
            );
            //openingHoursRepository.save(bOpeningHours);
            b.getOpeningHours().add(bOpeningHours);

            attractionRepository.save(b);

            bOpeningHours.setAttraction(b);
            openingHoursRepository.save(bOpeningHours);



            Attraction c = new Attraction();
            c.setName("Lehrpfad");
            c.setDescription("Gehen Sie bitte.");

            OpeningHours cOpeningHours = new OpeningHours(
                    "Täglich",
                    LocalTime.of(9, 0),
                    LocalTime.of(17, 0)
            );
            //openingHoursRepository.save(cOpeningHours);
            c.getOpeningHours().add(cOpeningHours);

            FilterTag naturTag = new FilterTag("Natur", c);
            c.getFilterTags().add(naturTag);

            attractionRepository.save(c);
            filterTagRepository.save(naturTag);

            cOpeningHours.setAttraction(c);
            openingHoursRepository.save(cOpeningHours);

            for(String tag : findTagService.readAllTags()) {
                logger.info("Neuer Tag hinzugefügt: " + tag);
            }
        }
    }
}
