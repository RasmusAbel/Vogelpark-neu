package de.bird.vogelpark.service.read;

import de.bird.vogelpark.beans.Attraction;
import de.bird.vogelpark.beans.FilterTag;
import de.bird.vogelpark.beans.OpeningHours;
import de.bird.vogelpark.dto.response.ReadAttractionsResponse;
import de.bird.vogelpark.repositories.AttractionRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadAttractionsServiceTest {

    @InjectMocks
    ReadAttractionsService service;

    @Mock
    AttractionRepository repository;

    @Test
    public void testReadAttractionsByTags() {
        List<String> tags = new ArrayList<>();
        tags.add("Natur");
        List<Attraction> attractions = new ArrayList<>();
        Attraction attraction = new Attraction();
        attraction.setName("Attraktion");
        Set<FilterTag> attractionTags = new HashSet<>();
        attractionTags.add(new FilterTag("Natur", attraction));
        attraction.setFilterTags(attractionTags);
        Set<OpeningHours> attractionOpeningHours = new HashSet<>();
        attractionOpeningHours.add(new OpeningHours("Monday", LocalTime.of(9, 0), LocalTime.of(12, 0)));
        attraction.setOpeningHours(attractionOpeningHours);
        attractions.add(attraction);

        when(repository.findAttractionsByTags(tags, tags.size())).thenReturn(attractions);

        List<ReadAttractionsResponse> response = service.readAttractionsByTags(tags);

        assertEquals(1, response.size());
        assertEquals(attraction.getName(), response.get(0).name());
        assertEquals(attractionOpeningHours.stream().findAny().get().getWeekday(), response.get(0).openingHoursResponses().get(0).weekday());
    }


    @Test
    public void testReadAllAttractions(){
        List<Attraction> attractions = new ArrayList<>();
        Attraction attraction1 = new Attraction();
        attraction1.setName("Attraktion1");
        Attraction attraction2 = new Attraction();
        attraction2.setName("Attraktion2");
        attractions.add(attraction1);
        attractions.add(attraction2);

        when(repository.findAll()).thenReturn(attractions);

        List<ReadAttractionsResponse> response = service.readAllAttractions();

        assertEquals(2, response.size());
        assertEquals(attraction1.getName(), response.get(0).name());
        assertEquals(attraction2.getName(), response.get(1).name());
    }


}