package de.bird.vogelpark.integrationtest.testHttpDelete;

import de.bird.vogelpark.BirdParkApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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
     * Testing if all DELETE-Functions exit with value 200 (OK)
     * and if response-body is not empty
     */
    @Test
    public void testDeleteAttraction() throws Exception {
//        mockMvc.perform(delete("/delete-attraction/")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .param("attractionName", "Flugkäfig"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isNotEmpty());
    }


}
