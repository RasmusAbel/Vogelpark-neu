package de.bird.vogelpark;

import de.bird.vogelpark.initializer.AttractionsInitializer;
import de.bird.vogelpark.initializer.BirdParkBasicInfoDBInitializer;
import de.bird.vogelpark.initializer.DatabaseDeleter;
import de.bird.vogelpark.initializer.ToursInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class BirdParkApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(BirdParkApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(BirdParkApplication.class, args);
	}

	BirdParkBasicInfoDBInitializer birdParkBasicInfoDBInitializer;
	AttractionsInitializer attractionsInitializer;

	ToursInitializer toursInitializer;
	DatabaseDeleter databaseDeleter;

	public BirdParkApplication(BirdParkBasicInfoDBInitializer birdParkBasicInfoDBInitializer,
							   AttractionsInitializer attractionsInitializer,
							   DatabaseDeleter databaseDeleter,
							   ToursInitializer toursInitializer) {
		this.birdParkBasicInfoDBInitializer = birdParkBasicInfoDBInitializer;
		this.attractionsInitializer = attractionsInitializer;
		this.databaseDeleter = databaseDeleter;
		this.toursInitializer = toursInitializer;
	}

	public void run(String... args) {
		logger.info("BirdParkApplication has been started...");

		//databaseDeleter.delete();

		//birdParkBasicInfoDBInitializer.initialize();
		//attractionsInitializer.initialize();
		//toursInitializer.initialize();
	}
}
