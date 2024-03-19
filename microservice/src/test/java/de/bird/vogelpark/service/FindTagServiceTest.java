package de.bird.vogelpark.service;

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
public class FindTagServiceTest {

        @InjectMocks
        FindTagService service;

        @Mock
        FilterTagRepository filterTagRepository;

        private final String FILTER_TAG = "testTag";

        @Test
        public void testGetAllTags(){
            List<FilterTag> filterTags = new ArrayList<>();
            String tagName = "tagName";
            Attraction attraction = new Attraction();
            FilterTag tag1 = new FilterTag(tagName, attraction);
            filterTags.add(tag1);

            when(filterTagRepository.findAll()).thenReturn(filterTags);

            List<String> response = service.getAllTags();

            assertEquals(tagName, response.get(0));
        }
    }

