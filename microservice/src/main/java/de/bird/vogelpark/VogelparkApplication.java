package de.bird.vogelpark;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class VogelparkApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(VogelparkApplication.class);

	private final PersonTabelle personTabelle;
	public VogelparkApplication (PersonTabelle personTabelle ) {this.personTabelle = personTabelle;}

	public static void main(String[] args) {
		SpringApplication.run(VogelparkApplication.class, args);
	}

	public void run(String... args) {
		logger.info("HAAAAALLLOOOOOO");

		personTabelle.save(new DBPerson());
		personTabelle.save(new DBPerson());
		personTabelle.save(new DBPerson());

		for(DBPerson pers : personTabelle.findAll()) {
			logger.info("Person-ID: {}", pers.getId());
		}
	}

}
