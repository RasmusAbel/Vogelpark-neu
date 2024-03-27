package de.bird.vogelpark.integrationtest.testHttpPost;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bird.vogelpark.BirdParkApplication;
import de.bird.vogelpark.dto.request.CreateTourRequest;
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
public class CreateTourIntTest {

    @Autowired
    private MockMvc mockMvc;


    /**
     * Testing POST-Function to create a tour
     * - exit with value 200 (OK)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateTour() throws Exception {
        String[] attractionNames = {"Aussichtsturm"};
        CreateTourRequest request = new CreateTourRequest("name", "description", 2555, "imageUrl", 9,0,12,0, attractionNames);


        mockMvc.perform(post("/create-tour/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Tour " + request.name() + " successfully created")));
    }

    /**
     * Testing to create a tour with a not existent attraction
     * - exit with value 404 (NOT_FOUND)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateTourWithNotExistentAttraction() throws Exception {
        String[] attractionNames = {"attractionName"};
        CreateTourRequest request = new CreateTourRequest("tourName", "description", 2555, "imageUrl", 9,0,12,0, attractionNames);

        mockMvc.perform(post("/create-tour/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Attraction with name " + attractionNames[0] + " does not exist and therefore cannot be added to tour " + request.name())));
    }

    /**
     * Testing to create a tour with invalid hours-input
     * - exit with value 400 (BAD_REQUEST)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateTourWithInvalidHoursInput() throws Exception {
        String[] attractionNames = {"Lehrpfad"};
        CreateTourRequest request = new CreateTourRequest("Name", "description", 2555, "imageUrl", 99,0,12,0, attractionNames);

        mockMvc.perform(post("/create-tour/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Hour must be between 0 and 23")));
    }

    /**
     * Testing to create a tour with invalid minutes-input
     * - exit with value 400 (BAD_REQUEST)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateTourWithInvalidMinutesInput() throws Exception {
        String[] attractionNames = {"Lehrpfad"};
        CreateTourRequest request = new CreateTourRequest("ame", "description", 2555, "imageUrl", 9,99,12,0, attractionNames);

        mockMvc.perform(post("/create-tour/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Minute must be between 0 and 59")));
    }

    /**
     * Testing to create a tour with startTime after endTime
     * - exit with value 400 (BAD_REQUEST)
     * - response-body contains correct message
     * @throws Exception
     */
    @Test
    public void testCreateTourStartTimeAfterEndTime() throws Exception {
        String[] attractionNames = {"Lehrpfad"};
        CreateTourRequest request = new CreateTourRequest("tourNdfghame", "description", 2555, "imageUrl", 19,0,12,0, attractionNames);

        mockMvc.perform(post("/create-tour/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Start time must be before end time")));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
