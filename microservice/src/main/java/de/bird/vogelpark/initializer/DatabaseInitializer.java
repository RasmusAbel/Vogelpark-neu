package de.bird.vogelpark.initializer;

import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializer {

    private final BirdParkBasicDataRepository birdParkBasicDataRepository;
    private final DatabaseDeleter databaseDeleter;
    private final BirdParkBasicInfoDBInitializer birdParkBasicInfoDBInitializer;
    private final AttractionsInitializer attractionsInitializer;
    private final ToursInitializer toursInitializer;

    public DatabaseInitializer(
            BirdParkBasicDataRepository birdParkBasicDataRepository,
            DatabaseDeleter databaseDeleter,
            BirdParkBasicInfoDBInitializer birdParkBasicInfoDBInitializer,
            AttractionsInitializer attractionsInitializer,
            ToursInitializer toursInitializer) {
        this.birdParkBasicDataRepository = birdParkBasicDataRepository;
        this.databaseDeleter = databaseDeleter;
        this.birdParkBasicInfoDBInitializer = birdParkBasicInfoDBInitializer;
        this.attractionsInitializer = attractionsInitializer;
        this.toursInitializer = toursInitializer;
    }

    /**
     * Tries to initialize the database with sample data if it is empty.
     */
    public void tryInitialize() {
        if(birdParkBasicDataRepository.count() == 0) {
            databaseDeleter.delete();
            birdParkBasicInfoDBInitializer.initialize();
            attractionsInitializer.initialize();
            toursInitializer.initialize();
        }
    }
}
