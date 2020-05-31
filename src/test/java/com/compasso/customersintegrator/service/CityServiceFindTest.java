package com.compasso.customersintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.repository.CityRepository;
import com.compasso.customersintegrator.util.FileUtils;
import com.compasso.customersintegrator.util.JsonUtils;

@SpringBootTest
@DisplayName("[CityService] - Tests case for find methods on City")
public class CityServiceFindTest {

    @Autowired
    private CityService service;

    @MockBean
    private CityRepository repository;

    @Test
    public void findById_shouldFindCityById() throws InstanceNotFoundException {
        /* Given */
        final City city = JsonUtils.fromString(
                FileUtils.readFile("./samples/existing_city_sample.json"),
                City.class
        );
        given(this.repository.findById(city.getId())).willReturn(Optional.of(city));

        /* When */
        final City existingCity = this.service.findById(city.getId());

        assertThat(existingCity).isEqualTo(city);
    }

    @Test
    public void findById_shouldThrowInstanceNotFoundException() {
        given(this.repository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> this.service.findById(99L));
    }
}
