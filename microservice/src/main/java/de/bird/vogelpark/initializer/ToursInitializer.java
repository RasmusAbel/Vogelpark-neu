package de.bird.vogelpark.initializer;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.Tour;
import de.bird.vogelpark.repositories.AttractionRepository;
import de.bird.vogelpark.repositories.TourRepository;
import org.springframework.stereotype.Service;

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
        a.setStartTime("10:00");
        a.setEndTime("12:00");
        a.setDuration("2:00");
        a.setImageUrl("");

        Attraction aussichtsturm = attractionRepository.findByName("Aussichtsturm").get();
        aussichtsturm.getTours().add(a);
        a.getAttractions().add(aussichtsturm);

        Attraction flugkaefig = attractionRepository.findByName("Flugkäfig").get();
        flugkaefig.getTours().add(a);
        a.getAttractions().add(flugkaefig);

        Attraction lehrpfad = attractionRepository.findByName("Lehrpfad").get();
        lehrpfad.getTours().add(a);
        a.getAttractions().add(lehrpfad);

        tourRepository.save(a);

        logger.info("Tours initialized");
    }
}
