package com.compasso.customersintegrator.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.management.InstanceAlreadyExistsException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import com.compasso.customersintegrator.domain.model.City;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@WebMvcTest(CityController.class)
@DisplayName("[CityController] - Test cases for POSTs on CityController")
public class CityControllerPostTest extends ControllerBase {

    @Test
    public void create_shouldCallServiceToCreateANewCityAndReturnCreated() throws Exception {
        final String requestBody = FileUtils.readFile("samples/city_sample.json");
        final City expectedCity = new City(1L, "Florianópolis", "SC");
        given(super.cityService.create(Mockito.any(City.class))).willReturn(expectedCity);

        super.mockMvc.perform(post(CITY_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.uri", is("http://localhost/v1/cities/1")));

        verify(super.cityService).create(JsonUtils.fromString(requestBody, City.class));
    }

    @Test
    public void create_shouldReturnBadRequestNameAndFederativeUnitAreRequired() throws Exception {
        final String requestBody = "{}";

        super.mockMvc.perform(post(CITY_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(super.cityService);
    }

    @Test
    public void create_shouldReturnConflictCityAlreadyExists() throws Exception {
        final String requestBody = FileUtils.readFile("samples/city_sample.json");
        final InstanceAlreadyExistsException e = new InstanceAlreadyExistsException("City already exists! You may check it on City ID: [1].");
        given(super.cityService.create(new City(null, "Florianópolis", "SC"))).willThrow(e);

        super.mockMvc.perform(post(CITY_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messages[0]", is(e.getMessage())));

    }

}
