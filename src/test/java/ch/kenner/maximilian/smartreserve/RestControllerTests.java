package ch.kenner.maximilian.smartreserve;


import ch.kenner.maximilian.smartreserve.model.service.Service;
import ch.kenner.maximilian.smartreserve.model.service.ServiceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestControllerTests {
    @Autowired
    private MockMvc api;

    @Autowired
    private ServiceRepository serviceRepository;

    @BeforeAll
    void setup() {
        Service serviceObj = new Service();
        serviceObj.setName("Haircut");
        serviceObj.setDescription("Haircut");
        serviceObj.setDurationSeconds(180L);
        serviceObj.setAfterServiceBreakDurationSeconds(2L);
        serviceRepository.save(serviceObj);
        Service serviceObj2 = new Service();
        serviceObj2.setName("Medium Haircut");
        serviceObj2.setDescription("Medium length haircut");
        serviceObj2.setDurationSeconds(180L);
        serviceObj2.setAfterServiceBreakDurationSeconds(2L);
        serviceRepository.save(serviceObj2);
    }

    @Test
    @Order(1)
    void testGetVehicles() throws Exception {
        api.perform(get("/api/service").with(csrf()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Haircut")));
    }

  /*  @Test
    @Order(2)
    @WithMockUser(authorities = "admin")
    void testSaveService() throws Exception {

        Service serviceObj2 = new Service();
        serviceObj2.setName("test Haircut");
        serviceObj2.setDescription("test haircut");
        serviceObj2.setDurationSeconds(270L);
        serviceObj2.setAfterServiceBreakDurationSeconds(20L);

        String body = new ObjectMapper().writeValueAsString(serviceObj2);

        api.perform(post("/api/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(csrf()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("test Haircut")));
    }*/
}
