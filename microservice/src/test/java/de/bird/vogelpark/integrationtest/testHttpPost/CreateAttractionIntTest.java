package de.bird.vogelpark.integrationtest.testHttpPost;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bird.vogelpark.BirdParkApplication;
import de.bird.vogelpark.dto.request.CreateAttractionRequest;
import de.bird.vogelpark.dto.request.CreateOpeningHoursRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BirdParkApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integrationtest.properties")
public class CreateAttractionIntTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Testing POST-Function to create an attraction
     * - exit with value 200 (OK)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateAttraction() throws Exception {
        final String ATTRACTION_NAME = "name";
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest("weekday", 9,0,12,0);
        CreateOpeningHoursRequest[] openingHoursResponses = {openingHoursRequest};
        String[] filterTagResponses = {"filterTag"};
        CreateAttractionRequest request = new CreateAttractionRequest(ATTRACTION_NAME, "description", openingHoursResponses, filterTagResponses);



        mockMvc.perform(post("/create-attraction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Attraction " + ATTRACTION_NAME + " successfully created")));
    }

    /**
     * Testing to create an existent attraction
     * - exit with value 400 (BAD_REQUEST)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateExistentAttraction() throws Exception {
        final String ATTRACTION_NAME = "Lehrpfad";
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest("weekday", 9,0,12,0);
        CreateOpeningHoursRequest[] openingHoursResponses = {openingHoursRequest};
        String[] filterTagResponses = {"filterTag"};
        CreateAttractionRequest request = new CreateAttractionRequest(ATTRACTION_NAME, "description", openingHoursResponses, filterTagResponses);

        mockMvc.perform(post("/create-attraction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Attraction " + ATTRACTION_NAME + " already exists")));
    }

    /**
     * Testing to create an attraction with invalid hours-input
     * - exit with value 400 (BAD_REQUEST)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateAttractionWithInvalidHoursInput() throws Exception {
        final String ATTRACTION_NAME = "name";
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest("weekday", 99,0,12,0);
        CreateOpeningHoursRequest[] openingHoursResponses = {openingHoursRequest};
        String[] filterTagResponses = {"filterTag"};
        CreateAttractionRequest request = new CreateAttractionRequest(ATTRACTION_NAME, "description", openingHoursResponses, filterTagResponses);

        mockMvc.perform(post("/create-attraction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Hour must be between 0 and 23")));
    }

    /**
     * Testing to create an attraction with invalid minutes-input
     * - exit with value 400 (BAD_REQUEST)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateAttractionWithInvalidMinutesInput() throws Exception {
        final String ATTRACTION_NAME = "name";
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest("weekday", 9,99,12,0);
        CreateOpeningHoursRequest[] openingHoursResponses = {openingHoursRequest};
        String[] filterTagResponses = {"filterTag"};
        CreateAttractionRequest request = new CreateAttractionRequest(ATTRACTION_NAME, "description", openingHoursResponses, filterTagResponses);

        mockMvc.perform(post("/create-attraction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Minute must be between 0 and 59")));
    }

    /**
     * Testing to create an attraction with startTime after endTime
     * - exit with value 400 (BAD_REQUEST)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateAttractionStartTimeAfterEndTime() throws Exception {
        final String ATTRACTION_NAME = "name";
        CreateOpeningHoursRequest openingHoursRequest = new CreateOpeningHoursRequest("weekday", 19,0,12,0);
        CreateOpeningHoursRequest[] openingHoursResponses = {openingHoursRequest};
        String[] filterTagResponses = {"filterTag"};
        CreateAttractionRequest request = new CreateAttractionRequest(ATTRACTION_NAME, "description", openingHoursResponses, filterTagResponses);

        mockMvc.perform(post("/create-attraction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Start time must be before end time")));
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
