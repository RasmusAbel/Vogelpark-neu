package de.bird.vogelpark.service.edit;

import de.bird.vogelpark.beans.BirdParkBasicInfo;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.dto.request.EditBirdParkBasicInfoRequest;
import de.bird.vogelpark.repositories.BirdParkBasicDataRepository;
import de.bird.vogelpark.repositories.OpeningHoursRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EditBirdParkBasicInfoServiceTest {

    @InjectMocks
    EditBirdParkBasicInfoService service;

    @Mock
    BirdParkBasicDataRepository birdParkBasicDataRepository;

    @Mock
    OpeningHoursRepository openingHoursRepository;

    private final Long[] OPENING_HOURS_IDS_TO_REMOVE = {1234L};


    /**
     * Testing all Edit-Functions
     */
    @Test
    public void testEditBasicInfo() {
        BirdParkBasicInfo basicInfo = new BirdParkBasicInfo();
        OpeningHours openingHours = new OpeningHours();
        List<OpeningHours> openingHoursList = new ArrayList<>();
        openingHoursList.add(openingHours);
        basicInfo.setOpeningHours(openingHoursList);
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest("WEEKDAY", 9,0, 12,0);
        EditBirdParkBasicInfoRequest request = new EditBirdParkBasicInfoRequest("NewName", "NewAddress", "NewDescription",new CreateOpeningHoursRequest[]{openingHoursRequest}, new Long[] {1234L}, "LogoUrl");

        List<BirdParkBasicInfo> basicInfoIterable = new ArrayList<>();
        basicInfoIterable.add(basicInfo);
        when(birdParkBasicDataRepository.findAll()).thenReturn(basicInfoIterable);
        when(openingHoursRepository.findById(OPENING_HOURS_IDS_TO_REMOVE[0])).thenReturn(Optional.of(openingHours));

        ResponseEntity<String> response = service.editBasicInfo(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bird park basic info successfully edited", response.getBody());
    }


}