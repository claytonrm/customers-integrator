package com.compasso.customersintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.management.InstanceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.domain.CityCriteria;
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

        /* Then */
        assertThat(existingCity).isEqualTo(city);
    }

    @Test
    public void findById_shouldThrowInstanceNotFoundException() {
        given(this.repository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> this.service.findById(99L));
    }

    @Test
    public void findByCriteria_shouldCallRepositoryToRetrieveCityByNameAndFederativeUnit() {
        /* Given */
        final List<City> expectedCities = Arrays.asList(new City(2L, "Joinville", "SC"));
        given(this.repository.findByNameAndFederativeUnit(anyString(), anyString())).willReturn(expectedCities);

        /* When */
        final List<City> cities = this.service.findByCriteria(CityCriteria.builder().name("Joinville").federativeUnit("SC").build());

        /* Then */
        assertThat(cities).isEqualTo(expectedCities);
    }
}
