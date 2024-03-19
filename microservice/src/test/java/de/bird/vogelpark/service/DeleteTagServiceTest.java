package de.bird.vogelpark.service;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.repositories.FilterTagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteTagServiceTest {

    @InjectMocks
    DeleteTagService service;

    @Mock
    FilterTagRepository filterTagRepository;

    private final String FILTER_TAG = "testTag";

    @Test
    public void testDeleteTag(){
        List<FilterTag> foundTagsList = new ArrayList<>();
        foundTagsList.add(new FilterTag(FILTER_TAG, new Attraction()));

        when(filterTagRepository.findAllByName(FILTER_TAG)).thenReturn(foundTagsList);

        ResponseEntity<String> response = service.deleteTag(FILTER_TAG);

        assertEquals("All occurrences of Tag " + FILTER_TAG + " successfully deleted", response.getBody());
    }

    @Test
    public void testDeleteNotExistantTag(){
        List<FilterTag> foundTagsListEmpty = new ArrayList<>();

        when(filterTagRepository.findAllByName(FILTER_TAG)).thenReturn(foundTagsListEmpty);

        ResponseEntity<String> response = service.deleteTag(FILTER_TAG);

        assertEquals("Tag " + FILTER_TAG + " is never used and therefore can't be deleted", response.getBody());
    }

}
