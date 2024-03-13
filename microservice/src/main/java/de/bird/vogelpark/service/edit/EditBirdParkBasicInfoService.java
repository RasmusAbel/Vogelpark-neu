package de.bird.vogelpark.service.edit;

import de.bird.vogelpark.beans.BirdParkBasicInfo;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.dto.request.EditBirdParkBasicInfoRequest;
import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class EditBirdParkBasicInfoService {
    private final BirdParkBasicDataRepository birdParkBasicDataRepository;

    private final OpeningHoursRepository openingHoursRepository;

    public EditBirdParkBasicInfoService(BirdParkBasicDataRepository birdParkBasicDataRepository,
                                        OpeningHoursRepository openingHoursRepository) {
        this.birdParkBasicDataRepository = birdParkBasicDataRepository;
        this.openingHoursRepository = openingHoursRepository;
    }

    public ResponseEntity<String> editBasicInfo(EditBirdParkBasicInfoRequest req) {
        //Get first and only element of birdParkBasicDataRepository
        BirdParkBasicInfo birdParkBasicInfo = birdParkBasicDataRepository.findAll().iterator().next();

        if(req.getNewName() != null) {
            birdParkBasicInfo.setName(req.getNewName());
        }

        if(req.getNewAddress() != null) {
            birdParkBasicInfo.setAddress(req.getNewAddress());
        }

        if(req.getNewDescription() != null) {
            birdParkBasicInfo.setDescription(req.getNewDescription());
        }

        if(req.getNewLogoUrl() != null) {
            birdParkBasicInfo.setLogoUrl(req.getNewLogoUrl());
        }

        //Neue Öffnungszeiten hinzufügen
        for(CreateOpeningHoursRequest createOpHoursReq : req.getOpeningHoursToAdd()) {
            OpeningHours openingHours = new OpeningHours(
                    createOpHoursReq.getWeekday(),
                    createOpHoursReq.getStartTime(),
                    createOpHoursReq.getEndTime(),
                    birdParkBasicInfo,
                    null
            );
            openingHoursRepository.save(openingHours);
            birdParkBasicInfo.getOpeningHours().add(openingHours);
        }

        //Zu löschende Öffnungszeiten entfernen
        for(Long opHoursId : req.getOpeningHourIdsToRemove()) {
            Optional<OpeningHours> foundOpHours = openingHoursRepository.findById(opHoursId);
            if(foundOpHours.isPresent()) {
                OpeningHours openingHours = foundOpHours.get();
                birdParkBasicInfo.getOpeningHours().remove(openingHours);
                openingHoursRepository.delete(openingHours);
            }
        }

        return ResponseEntity.ok("Bird park basic info successfully edited");
    }
}
