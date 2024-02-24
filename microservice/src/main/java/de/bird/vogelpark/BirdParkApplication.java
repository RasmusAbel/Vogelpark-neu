package de.bird.vogelpark;

import de.bird.vogelpark.initializer.AttractionsInitializer;
import de.bird.vogelpark.initializer.BirdParkBasicInfoDBInitializer;
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

	public BirdParkApplication(BirdParkBasicInfoDBInitializer birdParkBasicInfoDBInitializer,
							   AttractionsInitializer attractionsInitializer) {
		this.birdParkBasicInfoDBInitializer = birdParkBasicInfoDBInitializer;
		this.attractionsInitializer = attractionsInitializer;
	}

	public void run(String... args) {
		logger.info("BirdParkApplication has been started...");
		birdParkBasicInfoDBInitializer.initialize();
		attractionsInitializer.initialize();
	}

}
