package de.bird.vogelpark.controller;

import de.bird.vogelpark.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AdminController {

    private final DeleteAttractionService deleteAttractionService;

    private final CreateAttractionService createAttractionService;

    private final CreateTagService createTagService;

    private final DeleteTagService deleteTagService;


    public AdminController(DeleteAttractionService deleteAttractionService, CreateAttractionService createAttractionService, CreateTagService createTagService, DeleteTagService deleteTagService) {
        this.deleteAttractionService = deleteAttractionService;
        this.createAttractionService = createAttractionService;
        this.createTagService = createTagService;
        this.deleteTagService = deleteTagService;
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

    @DeleteMapping(path = "/delete-tag/")
    public void deleteTag(@RequestParam("tagName") String tagName){
        deleteTagService.deleteTag(tagName);
    }
}
