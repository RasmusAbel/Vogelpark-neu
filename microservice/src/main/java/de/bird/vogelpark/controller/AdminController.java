package de.bird.vogelpark.controller;

import de.bird.vogelpark.dto.request.*;
import de.bird.vogelpark.service.create.CreateAttractionService;
import de.bird.vogelpark.service.create.CreateTourService;
import de.bird.vogelpark.service.delete.DeleteAttractionService;
import de.bird.vogelpark.service.delete.DeleteTagService;
import de.bird.vogelpark.service.delete.DeleteTourService;
import de.bird.vogelpark.service.edit.EditAttractionService;
import de.bird.vogelpark.service.edit.EditBirdParkBasicInfoService;
import de.bird.vogelpark.service.edit.EditTourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final DeleteTourService deleteTourService;
    private final CreateTourService createTourService;


    public AdminController(DeleteAttractionService deleteAttractionService,
                           CreateAttractionService createAttractionService,
                           DeleteTagService deleteTagService,
                           EditAttractionService editAttractionService,
                           EditBirdParkBasicInfoService editBirdParkBasicInfoService,
                           EditTourService editTourService, DeleteTourService deleteTourService, CreateTourService createTourService) {
        this.deleteAttractionService = deleteAttractionService;
        this.createAttractionService = createAttractionService;
        this.deleteTagService = deleteTagService;
        this.editAttractionService = editAttractionService;
        this.editBirdParkBasicInfoService = editBirdParkBasicInfoService;
        this.editTourService = editTourService;
        this.deleteTourService = deleteTourService;
        this.createTourService = createTourService;
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/delete-attraction/")
    public ResponseEntity<String> deleteAttraction(@RequestParam("attractionName") String attractionName) {
        return deleteAttractionService.deleteAttraction(attractionName);
    }

    @PostMapping(path = "/create-attraction/")
    public ResponseEntity<String> createAttraction(@RequestBody CreateAttractionRequest createAttractionRequest) {
        return createAttractionService.createAttraction(createAttractionRequest);
    }
    @PostMapping(path = "/create-tour/")
    public ResponseEntity<String> createTour(@RequestBody CreateTourRequest createTourRequest) {
        return createTourService.createTour(createTourRequest);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/delete-tag/")
    public ResponseEntity<String> deleteTag(@RequestParam("tagName") String tagName) {
        return deleteTagService.deleteTag(tagName);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/delete-tour/")
    public ResponseEntity<String> deleteTour(@RequestParam("tourName") String tourName) {
        return deleteTourService.deleteTour(tourName);
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
