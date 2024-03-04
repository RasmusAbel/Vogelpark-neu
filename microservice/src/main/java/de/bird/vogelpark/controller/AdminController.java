package de.bird.vogelpark.controller;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.dto.ReadAttractionsByTagsResponse;
import de.bird.vogelpark.service.AttractionsService;
import de.bird.vogelpark.service.CreateAttractionService;
import de.bird.vogelpark.service.CreateTagService;
import de.bird.vogelpark.service.DeleteAttractionService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AdminController {

    private DeleteAttractionService deleteAttractionService;

    private CreateAttractionService createAttractionService;

    private AttractionsService attractionsService;

    private CreateTagService createTagService;


    public AdminController(DeleteAttractionService deleteAttractionService, CreateAttractionService createAttractionService, AttractionsService attractionsService, CreateTagService createTagService) {
        this.deleteAttractionService = deleteAttractionService;
        this.createAttractionService = createAttractionService;
        this.attractionsService = attractionsService;
        this.createTagService = createTagService;
    }

    @DeleteMapping(path = "/delete-attraction/")
    public void deleteAttraction(@RequestParam("name") String name) {
        deleteAttractionService.deleteAttraction(name);
    }

    @PostMapping(path = "/create-attraction/")
    public void createAttraction(@RequestParam("data") List<String> data){
        createAttractionService.createAttraction(data);
    }

    @PostMapping(path = "/create-tag/")
    public void createTag(@RequestParam("tagName") String tagName){
        createTagService.createTag(tagName);
    }
}
