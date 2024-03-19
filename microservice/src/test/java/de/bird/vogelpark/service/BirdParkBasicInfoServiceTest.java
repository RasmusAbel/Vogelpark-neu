package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.BirdParkBasicInfo;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.response.BirdParkBasicInfoResponse;
import de.bird.vogelpark.dto.response.OpeningHoursResponse;
import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BirdParkBasicInfoServiceTest {

    @InjectMocks
    BirdParkBasicInfoService service;

    @Mock
    BirdParkBasicDataRepository repository;

    private final String ADDRESS = "address";
    private final String DESCRIPTION = "description";
    private final String NAME = "name";
    private final String WEEKDAY = "weekday";
    private final String START_TIME = "startTime";
    private final String END_TIME = "endTime";


    @Test
    public void testReadBasicData(){
        List<BirdParkBasicInfo> birdParkBasicInfo = setUpBirdParkBasicInfo();

        when(repository.findAll()).thenReturn(birdParkBasicInfo);

        BirdParkBasicInfoResponse response = service.readBasicData();
        OpeningHoursResponse openingHoursResponse = response.getOpeningHoursResponses().get(0);

        assertEquals(ADDRESS, response.getAddress());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(NAME, response.getName());
        assertEquals(WEEKDAY, openingHoursResponse.getWeekday());
        assertEquals(START_TIME, openingHoursResponse.getStartTime());
        assertEquals(END_TIME, openingHoursResponse.getEndTime());
    }

    @Test
    public void testIllegalArgumentException(){
        List<BirdParkBasicInfo> birdParkBasicInfo = new ArrayList<>();

        when(repository.findAll()).thenReturn(birdParkBasicInfo);

        assertThrows(IllegalArgumentException.class, () -> service.readBasicData(), "No birdParkBasicInformation found in database.");
    }

    private List<BirdParkBasicInfo> setUpBirdParkBasicInfo() {
        List<BirdParkBasicInfo> birdParkBasicInfos = new ArrayList<>();
        List<OpeningHours> openingHoursList = new ArrayList<>();
        OpeningHours openingHours = new OpeningHours();
        openingHours.setWeekday(WEEKDAY);
        openingHours.setStartTime(START_TIME);
        openingHours.setEndTime(END_TIME);
        openingHoursList.add(openingHours);
        BirdParkBasicInfo basicInfo = new BirdParkBasicInfo();
        basicInfo.setAddress(ADDRESS);
        basicInfo.setDescription(DESCRIPTION);
        basicInfo.setName(NAME);
        basicInfo.setOpeningHours(openingHoursList);
        birdParkBasicInfos.add(basicInfo);
        return birdParkBasicInfos;
    }
}
