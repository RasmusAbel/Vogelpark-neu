package de.bird.vogelpark.controller;

import de.bird.vogelpark.dto.request.CreateAttractionRequest;
import de.bird.vogelpark.dto.request.EditAttractionRequest;
import de.bird.vogelpark.dto.request.EditBirdParkBasicInfoRequest;
import de.bird.vogelpark.dto.request.EditTourRequest;
import de.bird.vogelpark.service.create.CreateAttractionService;
import de.bird.vogelpark.service.delete.DeleteAttractionService;
import de.bird.vogelpark.service.delete.DeleteTagService;
import de.bird.vogelpark.service.edit.EditAttractionService;
import de.bird.vogelpark.service.edit.EditBirdParkBasicInfoService;
import de.bird.vogelpark.service.edit.EditTourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@CrossOrigin(origins = "*")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final DeleteAttractionService deleteAttractionService;
    private final CreateAttractionService createAttractionService;
    private final DeleteTagService deleteTagService;
    private final EditAttractionService editAttractionService;
    private final EditBirdParkBasicInfoService editBirdParkBasicInfoService;
    private final EditTourService editTourService;


    public AdminController(DeleteAttractionService deleteAttractionService,
                           CreateAttractionService createAttractionService,
                           DeleteTagService deleteTagService,
                           EditAttractionService editAttractionService,
                           EditBirdParkBasicInfoService editBirdParkBasicInfoService,
                           EditTourService editTourService) {
        this.deleteAttractionService = deleteAttractionService;
        this.createAttractionService = createAttractionService;
        this.deleteTagService = deleteTagService;
        this.editAttractionService = editAttractionService;
        this.editBirdParkBasicInfoService = editBirdParkBasicInfoService;
        this.editTourService = editTourService;
    }

    @DeleteMapping(path = "/delete-attraction/")
    public ResponseEntity<String> deleteAttraction(@RequestParam("attractionName") String attractionName) {
        return deleteAttractionService.deleteAttraction(attractionName);
    }

    @PostMapping(path = "/create-attraction/")
    public ResponseEntity<String> createAttraction(@RequestBody CreateAttractionRequest createAttractionRequest) {
        return createAttractionService.createAttraction(createAttractionRequest);
    }

    @DeleteMapping(path = "/delete-tag/")
    public ResponseEntity<String> deleteTag(@RequestParam("tagName") String tagName) {
        return deleteTagService.deleteTag(tagName);
    }

    @PutMapping(path = "/edit-attraction/")
    public ResponseEntity<String> editAttraction(@RequestBody EditAttractionRequest editAttractionRequest) {
        return editAttractionService.editAttraction(editAttractionRequest);
    }

    @PutMapping(path = "/edit-basic-info/")
    public ResponseEntity<String> editBasicInfo(@RequestBody EditBirdParkBasicInfoRequest editBasicInfoRequest) {
        return editBirdParkBasicInfoService.editBasicInfo(editBasicInfoRequest);
    }

    @PutMapping(path = "/edit-tour/")
    public ResponseEntity<String> editTour(@RequestBody EditTourRequest req) {
        return editTourService.editTour(req);
    }
}
