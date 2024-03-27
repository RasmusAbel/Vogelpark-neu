package de.bird.vogelpark.integrationtest.testHttpGet;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BirdParkApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integrationtest.properties")
public class HttpGetTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Testing if all GET-Functions exit with value 200 (OK)
     * and if response-body is not empty
     */
    @Test
    public void testGetAllAttractions() throws Exception {
        mockMvc.perform(get("/all-attractions/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetBirdParkBasicInfo() throws Exception {
        mockMvc.perform(get("/bird-park-basic-info/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetAllTags() throws Exception {
        mockMvc.perform(get("/all-tags/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetAllTours() throws Exception {
        mockMvc.perform(get("/all-tours/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetToursByAttractions() throws Exception {
        mockMvc.perform(get("/tours-by-attractions/")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("attractionName", "Aussichtsturm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }




}
