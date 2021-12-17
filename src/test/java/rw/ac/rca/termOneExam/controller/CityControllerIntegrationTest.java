package rw.ac.rca.termOneExam.controller;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.utils.APICustomResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getById_success() throws JSONException {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/cities/id/101", String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        JSONAssert.assertEquals("{\"id\":101,\"name\":\"Kigali\",\"weather\":24.0,\"fahrenheit\":0.0}",
                response.getBody(), false);
    }

    @Test
    public void getById_notFound(){
        ResponseEntity<APICustomResponse> response = this.restTemplate.getForEntity("/api/cities/id/203", APICustomResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("City not found with id 203", response.getBody().getMessage());
    }

    @Test
    public void getAll_success() throws JSONException {
        String response = this.restTemplate.getForObject("/api/cities/all", String.class);
        JSONAssert.assertEquals(
                "[{\"id\":101, \"name\":\"Kigali\", \"weather\":24}, {\"id\":102, \"name\":\"Musanze\", \"weather\":18}, {\"id\":103, \"name\":\"Rubavu\", \"weather\":20}, {\"id\":104, \"name\":\"Nyagatare\", \"weather\":28}]"
                ,response, false);
    }

    @Test
    public void saveItem_success(){
        CreateCityDTO cityDTO = new CreateCityDTO("Karongi", 18);
        ResponseEntity<City> response = this.restTemplate.postForEntity("/api/cities/add", cityDTO, City.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(64.4, response.getBody().getFahrenheit(), 0.0);
    }

    @Test
    public void saveItem_badRequest(){
        CreateCityDTO cityDTO = new CreateCityDTO("Kigali", 18);
        APICustomResponse customResponse = new APICustomResponse(false, "City name Kigali is registered already");
        ResponseEntity<APICustomResponse> response = this.restTemplate.postForEntity("/api/cities/add", cityDTO, APICustomResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(customResponse.getMessage(), response.getBody().getMessage());
    }
}
