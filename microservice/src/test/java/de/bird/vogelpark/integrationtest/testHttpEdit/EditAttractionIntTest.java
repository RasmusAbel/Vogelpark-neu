package de.bird.vogelpark.integrationtest.testHttpEdit;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bird.vogelpark.BirdParkApplication;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
import de.bird.vogelpark.dto.request.EditAttractionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BirdParkApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integrationtest.properties")
public class EditAttractionIntTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Testing PUT-Function to edit an attraction
     * - exit with value 200 (OK)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testEditAttraction() throws Exception {
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest("weekday", 9,0,12,0);
        CreateOpeningHoursRequest[] openingHours = {openingHoursRequest};
        String[] filterTagsToAdd = {"filterTag"};
        String[] filterTagsToRemove = {"Natur"};
        EditAttractionRequest request = new EditAttractionRequest("Lehrpfad", "newName", "newDescription", "newImageUrl", openingHours, new Long[]{}, filterTagsToAdd, filterTagsToRemove);


        mockMvc.perform(put("/edit-attraction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Attraction " + request.currentName() + " successfully edited")));
    }

    /**
     * Testing to edit a not existent attraction
     * - exit with value 404 (NOT_FOUND)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testEditNotExistentAttraction() throws Exception {
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest("weekday", 9,0,12,0);
        CreateOpeningHoursRequest[] openingHours = {openingHoursRequest};
        String[] filterTagsToAdd = {"filterTag"};
        String[] filterTagsToRemove = {"Natur"};
        EditAttractionRequest request = new EditAttractionRequest("c", "newName", "newDescription", "newImageUrl", openingHours, new Long[]{}, filterTagsToAdd, filterTagsToRemove);


        mockMvc.perform(put("/edit-attraction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Attraction with name " + request.currentName() + " does not exist")));
    }

    /**
     * Requests need to be converted to JSON-String
     * @param obj
     * @return
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
