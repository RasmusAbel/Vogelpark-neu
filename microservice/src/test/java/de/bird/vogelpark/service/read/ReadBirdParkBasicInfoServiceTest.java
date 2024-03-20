package de.bird.vogelpark.service.read;

import de.bird.vogelpark.beans.BirdParkBasicInfo;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.response.BirdParkBasicInfoResponse;
import de.bird.vogelpark.dto.response.OpeningHoursResponse;
import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadBirdParkBasicInfoServiceTest {

    @InjectMocks
    ReadBirdParkBasicInfoService service;

    @Mock
    BirdParkBasicDataRepository repository;

    private final String ADDRESS = "address";
    private final String DESCRIPTION = "description";
    private final String NAME = "name";
    private final String WEEKDAY = "weekday";
    private final LocalTime START_TIME = LocalTime.of(9, 0);
    private final LocalTime END_TIME = LocalTime.of(12, 0);


    @Test
    public void testReadBasicData(){
        List<BirdParkBasicInfo> birdParkBasicInfo = setUpBirdParkBasicInfo();

        when(repository.findAll()).thenReturn(birdParkBasicInfo);

        BirdParkBasicInfoResponse response = service.readBasicData();
        OpeningHoursResponse openingHoursResponse = response.openingHoursResponses().get(0);

        assertEquals(ADDRESS, response.address());
        assertEquals(DESCRIPTION, response.description());
        assertEquals(NAME, response.name());
        assertEquals(WEEKDAY, openingHoursResponse.weekday());
        assertEquals(START_TIME, openingHoursResponse.startTime());
        assertEquals(END_TIME, openingHoursResponse.endTime());
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
        OpeningHours openingHours = new OpeningHours(WEEKDAY, START_TIME, END_TIME);
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