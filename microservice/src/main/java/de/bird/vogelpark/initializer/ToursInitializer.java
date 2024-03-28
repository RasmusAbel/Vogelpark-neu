package de.bird.vogelpark.initializer;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.TourRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.logging.Logger;

@Service
public class ToursInitializer {
    Logger logger = Logger.getLogger(ToursInitializer.class.getName());

    private final TourRepository tourRepository;
    private final AttractionRepository attractionRepository;

    public ToursInitializer(TourRepository tourRepository, AttractionRepository attractionRepository) {
        this.tourRepository = tourRepository;
        this.attractionRepository = attractionRepository;
    }

    public void initialize() {
        logger.info("Initializing Tours");

        Tour a = new Tour();
        a.setName("Tour A");
        a.setDescription("Eine Tour durch den Park.");
        a.setPriceCents(500);
        a.setStartTime(LocalTime.of(10, 0));
        a.setEndTime(LocalTime.of(12, 0));
        a.setImageUrl("");

        if(attractionRepository.findByName("Aussichtsturm").isPresent()) {
            Attraction aussichtsturm = attractionRepository.findByName("Aussichtsturm").get();
            aussichtsturm.getTours().add(a);
            a.getAttractions().add(aussichtsturm);
        }

        if(attractionRepository.findByName("Flugkäfig").isPresent()) {
            Attraction flugkaefig = attractionRepository.findByName("Flugkäfig").get();
            flugkaefig.getTours().add(a);
            a.getAttractions().add(flugkaefig);
        }

        if(attractionRepository.findByName("Lehrpfad").isPresent()) {
            Attraction lehrpfad = attractionRepository.findByName("Lehrpfad").get();
            lehrpfad.getTours().add(a);
            a.getAttractions().add(lehrpfad);
        }

        tourRepository.save(a);

        logger.info("Tour A starts at: " + a.getStartTime());
        logger.info("Tour A ends at: " + a.getEndTime());
        logger.info("Tour A duration: " + a.getDurationString());

        Tour b = new Tour();
        b.setName("Tour B");
        b.setDescription("Eine andere Tour durch den Park.");
        b.setPriceCents(50);
        b.setStartTime(LocalTime.of(11, 0));
        b.setEndTime(LocalTime.of(15, 0));
        b.setImageUrl("");

        if(attractionRepository.findByName("Aussichtsturm").isPresent()) {
            Attraction aussichtsturm = attractionRepository.findByName("Aussichtsturm").get();
            aussichtsturm.getTours().add(b);
            b.getAttractions().add(aussichtsturm);
        }

        if(attractionRepository.findByName("Flugkäfig").isPresent()) {
            Attraction flugkaefig = attractionRepository.findByName("Flugkäfig").get();
            flugkaefig.getTours().add(b);
            b.getAttractions().add(flugkaefig);
        }

        if(attractionRepository.findByName("Lehrpfad").isPresent()) {
            Attraction lehrpfad = attractionRepository.findByName("Lehrpfad").get();
            lehrpfad.getTours().add(b);
            b.getAttractions().add(lehrpfad);
        }

        tourRepository.save(b);

        logger.info("Tour B starts at: " + b.getStartTime());
        logger.info("Tour B ends at: " + b.getEndTime());
        logger.info("Tour B duration: " + b.getDurationString());

        logger.info("Tours initialized");
    }
}
