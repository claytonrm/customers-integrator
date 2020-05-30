package com.compasso.customersintegrator.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@DisplayName("[CityController] - Test Cases for POSTs on CityController")
public class CityControllerPostTest extends CityControllerBase {

    @Test
    public void create_shouldCallServiceToCreateANewCityAndReturnCreated() throws Exception {
        final String requestBody = FileUtils.readFile("./samples/one_city_sample.json");
        final City expectedCity = new City(1L, "Florianópolis", "SC");
        given(super.service.create(Mockito.any(City.class))).willReturn(expectedCity);

        super.mockMvc.perform(post(TARGET_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.uri", is("http://localhost/v1/cities/1")));

        verify(super.service).create(JsonUtils.fromString(requestBody, City.class));
    }

    @Test
    public void create_shouldReturnBadRequestNameAndFederativeUnitAreRequired() throws Exception {
        final String requestBody = "{}";

        super.mockMvc.perform(post(TARGET_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(super.service);
    }

}
