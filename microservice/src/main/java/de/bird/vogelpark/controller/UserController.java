package de.bird.vogelpark.controller;

import de.bird.vogelpark.dto.ReadAttractionsByTagsResponse;
import de.bird.vogelpark.dto.BirdParkBasicInfoResponse;
import de.bird.vogelpark.service.AttractionsService;
import de.bird.vogelpark.service.BirdParkBasicInfoService;
import de.bird.vogelpark.service.TagService;
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

    private final TagService tagService;

    public UserController(BirdParkBasicInfoService birdParkBasicInfoService,
                          AttractionsService attractionsService,
                          TagService tagService) {
        this.birdParkBasicInfoService = birdParkBasicInfoService;
        this.attractionsService = attractionsService;
        this.tagService = tagService;
    }
    @GetMapping(path = "/bird-park-basic-info/")
    public BirdParkBasicInfoResponse readBasicInfo() {
        return birdParkBasicInfoService.readBasicData();
    }

    @GetMapping(path = "/attractions/")
    public List<ReadAttractionsByTagsResponse> getAttractionsByTags(@RequestParam("tags") List<String> tags) {
        return attractionsService.readAttractionsByTags(tags);
    }

    @GetMapping(path = "/all-tags/")
    public List<String> getAllTags() {
        return tagService.getAllTags();
    }
}
