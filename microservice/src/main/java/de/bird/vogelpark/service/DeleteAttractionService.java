package de.bird.vogelpark.service;

import de.bird.vogelpark.repositories.AttractionRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteAttractionService {
    private AttractionRepository attractionRepository;

    public DeleteAttractionService(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    public void deleteAttraction(String name) {
        attractionRepository.delete(attractionRepository.findAttractionByName(name));
    }
}
