package de.bird.vogelpark.service.edit;

import de.bird.vogelpark.beans.BirdParkBasicInfo;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.dto.request.EditBirdParkBasicInfoRequest;
import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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

        if(req.newName() != null) {
            birdParkBasicInfo.setName(req.newName());
        }

        if(req.newAddress() != null) {
            birdParkBasicInfo.setAddress(req.newAddress());
        }

        if(req.newDescription() != null) {
            birdParkBasicInfo.setDescription(req.newDescription());
        }

        if(req.newLogoUrl() != null) {
            birdParkBasicInfo.setLogoUrl(req.newLogoUrl());
        }

        removeOpeningHours(birdParkBasicInfo, req.openingHourIdsToRemove());
        addOpeningHours(birdParkBasicInfo, req.openingHoursToAdd());

        birdParkBasicDataRepository.save(birdParkBasicInfo);

        return ResponseEntity.ok("Grunddaten des Vogelparks wurden erfolgreich bearbeitet.");
    }

    /**
     * Removes opening hours with the given ids from the bird park basic info
     * @param birdPark bird park basic info to remove opening hours from
     * @param opHourIdsToRemove ids of opening hours to remove
     */
    private void removeOpeningHours(BirdParkBasicInfo birdPark, Long[] opHourIdsToRemove) {
        for(Long opHoursId : opHourIdsToRemove) {
            Optional<OpeningHours> foundOpHours = openingHoursRepository.findById(opHoursId);
            if(foundOpHours.isPresent()) {
                OpeningHours openingHours = foundOpHours.get();
                birdPark.getOpeningHours().remove(openingHours);
                openingHoursRepository.delete(openingHours);
            }
        }
    }

    /**
     * Adds the passed opening hours to the bird park basic info
     * @param birdPark bird park basic info to add opening hours to
     * @param opHoursToAdd opening hours to add
     */
    private void addOpeningHours(BirdParkBasicInfo birdPark, CreateOpeningHoursRequest[] opHoursToAdd) {
        for(CreateOpeningHoursRequest createOpHoursReq : opHoursToAdd) {
            OpeningHours openingHours = new OpeningHours(
                    createOpHoursReq.weekday(),
                    LocalTime.of(createOpHoursReq.startTimeHour(), createOpHoursReq.startTimeMinute()),
                    LocalTime.of(createOpHoursReq.endTimeHour(), createOpHoursReq.endTimeMinute()),
                    birdPark,
                    null
            );
            openingHoursRepository.save(openingHours);
            birdPark.getOpeningHours().add(openingHours);
        }
    }
}
