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

import java.time.LocalTime;

@Service
public class BirdParkBasicInfoDBInitializer {
    private static final Logger logger = LoggerFactory.getLogger(BirdParkApplication.class);

    private BirdParkBasicDataRepository birdParkBasicDataRepository;
    private OpeningHoursRepository openingHoursRepository;

    public BirdParkBasicInfoDBInitializer(BirdParkBasicDataRepository birdParkBasicDataRepository, OpeningHoursRepository openingHoursRepository) {
        this.birdParkBasicDataRepository = birdParkBasicDataRepository;
        this.openingHoursRepository = openingHoursRepository;
    }

    public void initialize() {
        if(birdParkBasicDataRepository.count() <= 0) {
            BirdParkBasicInfo vogelpark = new BirdParkBasicInfo();
            vogelpark.setName("Hamelner Vögelforst");
            vogelpark.setDescription("Wir sind gut zu Vögeln");
            vogelpark.setAddress("Waldweg 1, 54321 Musterstadt");
            vogelpark.setLogoUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTf42BZfnWWrBLVooH4pCIPvcOuf1XtcbcvGL_NWrv2MA&s");

            OpeningHours taeglich = new OpeningHours("Täglich",
                    LocalTime.of(8, 0),
                    LocalTime.of(19, 0),
                    vogelpark,
                    null);
            vogelpark.getOpeningHours().add(taeglich);

            birdParkBasicDataRepository.save(vogelpark);
            openingHoursRepository.save(taeglich);

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
