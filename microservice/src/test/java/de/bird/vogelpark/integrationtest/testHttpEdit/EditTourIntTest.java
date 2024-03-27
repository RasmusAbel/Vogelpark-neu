package de.bird.vogelpark.integrationtest.testHttpEdit;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bird.vogelpark.BirdParkApplication;
import de.bird.vogelpark.dto.request.EditTourRequest;
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
public class EditTourIntTest {

    @Autowired
    private MockMvc mockMvc;


    /**
     * Testing PUT-Function to edit a tour
     * - exit with value 200 (OK)
     * - response-body contains correct message
     *
     * @throws Exception
     */
    @Test
    public void testEditTour() throws Exception {
        String[] attractionNamesToAdd = {"Lehrpfad"};
        String[] attractionNamesToRemove = {"Lehrpfad"};
        EditTourRequest request = new EditTourRequest("Tour B",
                "newName",
                "newDescription",
                321,
                "newImageUrl",
                9,
                0,
                12,
                0,
                attractionNamesToAdd,
                attractionNamesToRemove);
        mockMvc.perform(put("/edit-tour/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Tour " + request.currentName() + " successfully edited")));
    }

    /**
     * Requests need to be converted to JSON-String
     *
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
