package de.bird.vogelpark.initializer;

import de.bird.vogelpark.BirdParkApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import de.bird.vogelpark.repositories.*;

@Service
public class DatabaseDeleter {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseDeleter.class);
    private final AttractionRepository attractionRepository;
    private final BirdParkBasicDataRepository birdParkBasicDataRepository;
    private final FilterTagRepository filterTagRepository;
    private final OpeningHoursRepository openingHoursRepository;

    private final TourRepository tourRepository;

    public DatabaseDeleter(AttractionRepository attractionRepository,
                           BirdParkBasicDataRepository birdParkBasicDataRepository,
                           FilterTagRepository filterTagRepository,
                           OpeningHoursRepository openingHoursRepository,
                           TourRepository tourRepository) {
        this.attractionRepository = attractionRepository;
        this.birdParkBasicDataRepository = birdParkBasicDataRepository;
        this.filterTagRepository = filterTagRepository;
        this.openingHoursRepository = openingHoursRepository;
        this.tourRepository = tourRepository;
    }

    /**
     * Löscht den Inhalt aller Tabellen der DB. Die
     * DB und die Tabellen selbst bleiben erhalten.
     */
    public void delete() {
        logger.info("Lösche den Inhalt der Datenbank...");

        filterTagRepository.deleteAll();
        openingHoursRepository.deleteAll();
        attractionRepository.deleteAll();
        birdParkBasicDataRepository.deleteAll();

        logger.info("Der Inhalt der Datenbank wurde gelöscht.");
    }
}
