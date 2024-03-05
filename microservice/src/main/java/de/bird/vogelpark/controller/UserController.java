package de.bird.vogelpark.controller;

import de.bird.vogelpark.dto.response.ReadAttractionsResponse;
import de.bird.vogelpark.dto.response.BirdParkBasicInfoResponse;
import de.bird.vogelpark.service.AttractionsService;
import de.bird.vogelpark.service.BirdParkBasicInfoService;
import de.bird.vogelpark.service.FindTagService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    private BirdParkBasicInfoService birdParkBasicInfoService;
    
    private AttractionsService attractionsService;

    private final FindTagService findTagService;

    public UserController(BirdParkBasicInfoService birdParkBasicInfoService,
                          AttractionsService attractionsService,
                          FindTagService findTagService) {
        this.birdParkBasicInfoService = birdParkBasicInfoService;
        this.attractionsService = attractionsService;
        this.findTagService = findTagService;
    }
    @GetMapping(path = "/bird-park-basic-info/")
    public BirdParkBasicInfoResponse readBasicInfo() {
        return birdParkBasicInfoService.readBasicData();
    }

    @GetMapping(path = "/attractions-by-tags/")
    public List<ReadAttractionsResponse> getAttractionsByTags(@RequestParam("tag") List<String> tags) {
        return attractionsService.readAttractionsByTags(tags);
    }

    @GetMapping(path = "/all-attractions/")
    public List<ReadAttractionsResponse> getAllAttractions() {
        return attractionsService.readAllAttractions();
    }

    @GetMapping(path = "/all-tags/")
    public List<String> getAllTags() {
        return findTagService.getAllTags();
    }
}
