package de.bird.vogelpark.service.read;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadTagServiceTest {

        @InjectMocks
        ReadTagService service;

        @Mock
        FilterTagRepository filterTagRepository;

        @Test
        public void testReadAllTags(){
            List<FilterTag> filterTags = new ArrayList<>();
            String tagName = "tagName";
            Attraction attraction = new Attraction();
            FilterTag tag1 = new FilterTag(tagName, attraction);
            filterTags.add(tag1);

            when(filterTagRepository.findAll()).thenReturn(filterTags);

            List<String> response = service.readAllTags();

            assertEquals(tagName, response.get(0));
        }
    }
