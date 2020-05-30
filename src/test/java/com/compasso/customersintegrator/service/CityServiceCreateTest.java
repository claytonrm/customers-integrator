package com.compasso.customersintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.compasso.customersintegrator.model.City;

@DisplayName("[CityService] - Unit Tests for create City")
public class CityServiceCreateTest extends CityServiceBase {

    @Test
    public void create_shouldCallRepositoryToCreateANewCity() {
        /* Given */
        final City expectedCity = new City(1L, "Gravatal", "SC");
        given(super.repository.save(Mockito.any(City.class))).willReturn(expectedCity);

        /* When */
        final City createdCity = super.service.create(new City(null, "Gravatal", "SC"));

        /* Then */
        assertThat(createdCity).isEqualTo(expectedCity);
    }

    @Test
    @DisplayName("Waiting for implementation")
    public void create_shouldCallRepositoryAndThrownConflictErrorCityAlreadyExists() {

    }

}
