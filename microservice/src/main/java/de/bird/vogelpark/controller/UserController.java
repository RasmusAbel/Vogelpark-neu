package de.bird.vogelpark.controller;

import de.bird.vogelpark.dto.ReadAttractionsByTagsResponse;
import de.bird.vogelpark.dto.BirdParkBasicInfoResponse;
import de.bird.vogelpark.service.AttractionsService;
import de.bird.vogelpark.service.BirdParkBasicInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private BirdParkBasicInfoService birdParkBasicInfoService;
    
    private AttractionsService attractionsService;

    public UserController(BirdParkBasicInfoService birdParkBasicInfoService,
                          AttractionsService attractionsService) {
        this.birdParkBasicInfoService = birdParkBasicInfoService;
        this.attractionsService = attractionsService;
    }
    @GetMapping(path = "/bird-park-basic-info/")
    public BirdParkBasicInfoResponse readBasicInfo() {
        return birdParkBasicInfoService.readBasicData();
    }

    @GetMapping(path = "/attractions/")
    public List<ReadAttractionsByTagsResponse> getAttractionsByTags(@RequestParam("tags") List<String> tags) {
        return attractionsService.readAttractionsByTags(tags);
    }
}
