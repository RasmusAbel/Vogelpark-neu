package de.bird.vogelpark.service.read;

import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.beans.BirdParkBasicInfo;
import de.bird.vogelpark.dto.response.OpeningHoursResponse;
import de.bird.vogelpark.dto.response.BirdParkBasicInfoResponse;
import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class ReadBirdParkBasicInfoService {
    private BirdParkBasicDataRepository repository;

    public ReadBirdParkBasicInfoService(BirdParkBasicDataRepository repository) {
        this.repository = repository;
    }

    public BirdParkBasicInfoResponse readBasicData() {
        BirdParkBasicInfo data = null;
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
        List<OpeningHoursResponse> openingHoursResponses = new ArrayList<>();

        for(OpeningHours dataOpeningHours : data.getOpeningHours()) {
            openingHoursResponses.add(new OpeningHoursResponse(
                    dataOpeningHours.getId(),
                    dataOpeningHours.getWeekday(),
                    dataOpeningHours.getStartTime(),
                    dataOpeningHours.getEndTime())
            );
        }

        return new BirdParkBasicInfoResponse(
                data.getName(),
                data.getDescription(),
                data.getAddress(),
                openingHoursResponses,
                data.getLogoUrl()
        );
    }
}
