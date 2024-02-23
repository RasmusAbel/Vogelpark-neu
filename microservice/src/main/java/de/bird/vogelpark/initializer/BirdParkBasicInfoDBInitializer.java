package de.bird.vogelpark.initializer;

import de.bird.vogelpark.BirdParkApplication;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.beans.BirdParkBasicInfo;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BirdParkBasicInfoDBInitializer {
    private static final Logger logger = LoggerFactory.getLogger(BirdParkApplication.class);

    @Autowired
    private BirdParkBasicDataRepository birdParkBasicDataRepository;
    @Autowired
    private OpeningHoursRepository openingHoursRepository;

    public void initialize() {
        if(birdParkBasicDataRepository.count() <= 0) {
            BirdParkBasicInfo vogelpark = new BirdParkBasicInfo();
            vogelpark.setName("Hamelner Vögelforst");
            vogelpark.setDescription("Wir sind gut zu Vögeln");
            vogelpark.setAddress("Waldweg 1, 54321 Musterstadt");

            OpeningHours montag = new OpeningHours("Montag", "9:00", "18:00", vogelpark, null);
            OpeningHours dienstag = new OpeningHours("Dienstag", "9:00", "18:00", vogelpark, null);
            OpeningHours mittwoch = new OpeningHours("Mittwoch", "9:00", "18:00", vogelpark, null);
            OpeningHours donnerstag = new OpeningHours("Donnerstag", "9:00", "18:00", vogelpark, null);
            OpeningHours freitag = new OpeningHours("Freitag", "9:00", "18:00", vogelpark, null);
            OpeningHours samstag = new OpeningHours("Samstag", "9:00", "18:00", vogelpark, null);
            OpeningHours sonntag = new OpeningHours("Sonntag", "9:00", "18:00", vogelpark, null);

            birdParkBasicDataRepository.save(vogelpark);

            openingHoursRepository.save(montag);
            openingHoursRepository.save(dienstag);
            openingHoursRepository.save(mittwoch);
            openingHoursRepository.save(donnerstag);
            openingHoursRepository.save(freitag);
            openingHoursRepository.save(samstag);
            openingHoursRepository.save(sonntag);

            logger.info("Grunddaten des Vogelparks wurden initial befuellt.");
        }

        logger.info("Grunddaten:");

        for (BirdParkBasicInfo birdParkBasicInfo : birdParkBasicDataRepository.findAll()) {
            logger.info(birdParkBasicInfo.toString());
        }

        for (OpeningHours openingHours : openingHoursRepository.findAll()) {
            logger.info(openingHours.toString());
        }
    }
}
