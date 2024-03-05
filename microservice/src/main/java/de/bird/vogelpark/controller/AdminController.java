package de.bird.vogelpark.controller;

import de.bird.vogelpark.dto.request.CreateAttractionRequest;
import de.bird.vogelpark.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> deleteAttraction(@RequestParam("attractionName") String attractionName) {
        return deleteAttractionService.deleteAttraction(attractionName);
    }

    @PostMapping(path = "/create-attraction/")
    public ResponseEntity<String> createAttraction(@RequestBody CreateAttractionRequest createAttractionRequest) {
        return createAttractionService.createAttraction(createAttractionRequest);
    }

    /*@PostMapping(path = "/create-tag/")
    public ResponseEntity<String> createTag(@RequestParam("tagName") String tagName) {
        return createTagService.createTag(tagName);
    }
     */

    @DeleteMapping(path = "/delete-tag/")
    public ResponseEntity<String> deleteTag(@RequestParam("tagName") String tagName) {
        return deleteTagService.deleteTag(tagName);
    }
}
