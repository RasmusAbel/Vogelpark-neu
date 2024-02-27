package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.beans.BirdParkBasicInfo;
import de.bird.vogelpark.dto.OpeningHoursResponse;
import de.bird.vogelpark.dto.BirdParkBasicInfoResponse;
import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BirdParkBasicInfoService {
    private BirdParkBasicDataRepository repository;

    public BirdParkBasicInfoService(BirdParkBasicDataRepository repository) {
        this.repository = repository;
    }

    public BirdParkBasicInfoResponse readBasicData() {
        BirdParkBasicInfo data = null;
        BirdParkBasicInfoResponse response;
        Iterable<BirdParkBasicInfo> iterator = repository.findAll();
        for(BirdParkBasicInfo current : iterator) {
            data = current;
            break;
        }
        if (data == null) {
            throw new IllegalArgumentException("No birdParkBasicInformation found in database.");
        }

        return mapData2Response(data);
    }

    private BirdParkBasicInfoResponse mapData2Response(BirdParkBasicInfo data) {
        BirdParkBasicInfoResponse response = new BirdParkBasicInfoResponse();
        //List<OpeningHoursResponse> openingHoursResponses = new ArrayList<>();

        for(OpeningHours dataOpeningHours : data.getOpeningHours()) {
            response.getOpeningHoursResponses().add(new OpeningHoursResponse(
                    dataOpeningHours.getWeekday(),
                    dataOpeningHours.getStartTime(),
                    dataOpeningHours.getEndTime())
            );
        }

        response.setName(data.getName());
        response.setDescription(data.getDescription());
        response.setAddress(data.getAddress());

        return response;
    }
}
