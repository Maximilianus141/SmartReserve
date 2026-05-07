package ch.kenner.maximilian.smartreserve;


import ch.kenner.maximilian.smartreserve.model.service.Service;
import ch.kenner.maximilian.smartreserve.model.service.ServiceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

        String accessToken = obtainAccessToken();

        api.perform(get("/api/vehicle").header("Authorization", "Bearer " + accessToken)
                        .with(csrf()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("BL 123")));
    }

    @Test
    @Order(1)
    void testSaveService() throws Exception {

        Service serviceObj2 = new Service();
        serviceObj2.setName("test Haircut");
        serviceObj2.setDescription("test haircut");
        serviceObj2.setDurationSeconds(270L);
        serviceObj2.setAfterServiceBreakDurationSeconds(20L);

        String accessToken = obtainAccessToken();
        String body = new ObjectMapper().writeValueAsString(serviceObj2);

        api.perform(post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + accessToken)
                        .with(csrf()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Test REST")));
    }

    private String obtainAccessToken() {

        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "client_id=smartreserve&" +
                "grant_type=password&" +
                "scope=openid profile roles offline_access&" +
                "username=admin&" +
                "password=admin";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> resp = rest.postForEntity("http://localhost:8080/realms/ILV/protocol/openid-connect/token", entity, String.class);

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resp.getBody()).get("access_token").toString();
    }
}
