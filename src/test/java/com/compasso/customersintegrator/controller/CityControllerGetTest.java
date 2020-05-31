package com.compasso.customersintegrator.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@WebMvcTest(CityController.class)
@DisplayName("[CityController] - Test cases for GETs on CityController")
public class CityControllerGetTest extends ControllerBase {

    @Test
    public void get_shouldFindCityByResourceIdAndReturnSuccess() throws Exception {
        final String expectedResponse = FileUtils.readFile("samples/existing_city_sample.json");
        final City existingCity = JsonUtils.fromString(expectedResponse, City.class);
        given(super.cityService.findById(anyLong())).willReturn(existingCity);

        super.mockMvc.perform(get(CITY_RELATIVE_PATH + existingCity.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void get_shouldReturnNotFound() throws Exception {
        given(super.cityService.findById(anyLong())).willThrow(InstanceNotFoundException.class);

        super.mockMvc.perform(get(CITY_RELATIVE_PATH + "44").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }


}
