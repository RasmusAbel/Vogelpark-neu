package de.bird.vogelpark.integrationtest.testHttpDelete;

import de.bird.vogelpark.BirdParkApplication;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BirdParkApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integrationtest.properties")
public class HttpDeleteIntTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Testing to delete an attraction
     * - exit with value 200 (OK)
     * - response-body contains correct message
     */
    @Test
    public void testDeleteAttraction() throws Exception {
        final String NAME = "Flugkäfig";

        mockMvc.perform(delete("/delete-attraction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("attractionName", NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Attraction " + NAME + " successfully deleted")));
    }

    /**
     * Testing to delete a not existent attraction
     * - exit with value 404 (NOT_FOUND)
     * - response-body contains correct message
     */
    @Test
    public void testDeleteNotExistentAttraction() throws Exception {
        final String NAME = "attractionName";

        mockMvc.perform(delete("/delete-attraction/")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("attractionName", NAME))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Attraction " + NAME + " does not exist and can't be deleted")));
    }

    /**
     * Testing to delete a tag
     * - exit with value 200 (OK)
     * - response-body contains correct message
     */
    @Test
    public void testDeleteTag() throws Exception {
        final String NAME = "Natur";

        mockMvc.perform(delete("/delete-tag/")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("tagName", NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("All occurrences of Tag " + NAME + " successfully deleted")));
    }

    /**
     * Testing to delete a not existent tag
     * - exit with value 404 (NOT_FOUND)
     * - response-body contains correct message
     */
    @Test
    public void testDeleteNotExistentTag() throws Exception {
        final String NAME = "tagName";

        mockMvc.perform(delete("/delete-tag/")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("tagName", NAME))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Tag " + NAME + " is never used and therefore can't be deleted")));
    }

    /**
     * Testing to delete a tour
     * - exit with value 200 (OK)
     * - response-body contains correct message
     */
    @Test
    public void testDeleteTour() throws Exception {
        final String NAME = "Tour A";

        mockMvc.perform(delete("/delete-tour/")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("tourName", NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Tour " + NAME + " successfully deleted")));
    }

    /**
     * Testing to delete a not existent tour
     * - exit with value 404 (NOT_FOUND)
     * - response-body contains correct message
     */
    @Test
    public void testDeleteNotExistentTour() throws Exception {
        final String NAME = "tourName";

        mockMvc.perform(delete("/delete-tour/")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("tourName", NAME))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Tour " + NAME + " does not exist and can't be deleted")));
    }




}
