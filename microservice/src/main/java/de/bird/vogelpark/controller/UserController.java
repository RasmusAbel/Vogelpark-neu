package de.bird.vogelpark.controller;

import de.bird.vogelpark.dto.response.ReadAttractionsResponse;
import de.bird.vogelpark.dto.response.BirdParkBasicInfoResponse;
import de.bird.vogelpark.dto.response.ReadTourResponse;
import de.bird.vogelpark.service.read.ReadAttractionsService;
import de.bird.vogelpark.service.read.ReadBirdParkBasicInfoService;
import de.bird.vogelpark.service.read.ReadTagService;
import de.bird.vogelpark.service.read.ReadToursService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    private ReadBirdParkBasicInfoService birdParkBasicInfoService;
    private ReadAttractionsService readAttractionsService;
    private final ReadTagService findTagService;
    private final ReadToursService readToursService;

    public UserController(ReadBirdParkBasicInfoService birdParkBasicInfoService,
                          ReadAttractionsService readAttractionsService,
                          ReadTagService findTagService,
                          ReadToursService readToursService) {
        this.birdParkBasicInfoService = birdParkBasicInfoService;
        this.readAttractionsService = readAttractionsService;
        this.findTagService = findTagService;
        this.readToursService = readToursService;
    }
    @GetMapping(path = "/bird-park-basic-info/")
    public BirdParkBasicInfoResponse readBasicInfo() {
        return birdParkBasicInfoService.readBasicData();
    }

    @GetMapping(path = "/attractions-by-tags/")
    public List<ReadAttractionsResponse> getAttractionsByTags(@RequestParam("tag") List<String> tags) {
        return readAttractionsService.readAttractionsByTags(tags);
    }

    @GetMapping(path = "/all-attractions/")
    public List<ReadAttractionsResponse> getAllAttractions() {
        return readAttractionsService.readAllAttractions();
    }

    @GetMapping(path = "/all-tags/")
    public List<String> getAllTags() {
        return findTagService.readAllTags();
    }

    @GetMapping(path = "all-tours/")
    public List<ReadTourResponse> getAllTours() {
        return readToursService.readAllTours();
    }
}
