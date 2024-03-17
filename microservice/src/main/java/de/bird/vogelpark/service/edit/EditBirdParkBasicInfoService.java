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

        removeOpeningHours(birdParkBasicInfo, req.getOpeningHourIdsToRemove());
        addOpeningHours(birdParkBasicInfo, req.getOpeningHoursToAdd());

        birdParkBasicDataRepository.save(birdParkBasicInfo);

        return ResponseEntity.ok("Bird park basic info successfully edited");
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
                    createOpHoursReq.getWeekday(),
                    createOpHoursReq.getStartTime(),
                    createOpHoursReq.getEndTime(),
                    birdPark,
                    null
            );
            openingHoursRepository.save(openingHours);
            birdPark.getOpeningHours().add(openingHours);
        }
    }
}
