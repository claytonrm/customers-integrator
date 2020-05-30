package com.compasso.customersintegrator.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.service.CityService;

import util.FileUtils;
import util.JsonUtils;

@WebMvcTest(CityController.class)
@DisplayName("[CityController] - Test Cases for City Controller")
public class CityControllerTest {

    private static final String TARGET_RELATIVE_PATH = "/v1/cities";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @Test
    public void create_shouldCallServiceToCreateANewCityAndReturnCreated() throws Exception {
        final String requestBody = FileUtils.readFile("./samples/one_city_sample.json");
        final City expectedCity = new City("1", "Florianópolis", "SC");
        given(this.cityService.create(Mockito.any(City.class))).willReturn(expectedCity);

        this.mockMvc.perform(post(TARGET_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.uri", is("http://localhost/v1/cities/1")));

        verify(this.cityService).create(JsonUtils.fromString(requestBody, City.class));
    }

    @Test
    @Disabled("Define model validation first.")
    public void create_shouldReturnBadRequestNameAndFederativeUnitAreRequired() throws Exception {
        final String requestBody = "{}";

        this.mockMvc.perform(post(TARGET_RELATIVE_PATH).content(requestBody).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.cityService);
    }

}
