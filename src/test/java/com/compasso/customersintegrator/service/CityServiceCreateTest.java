package com.compasso.customersintegrator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.management.InstanceAlreadyExistsException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.domain.model.City;
import com.compasso.customersintegrator.repository.CityRepository;

@SpringBootTest
@DisplayName("[CityService] - Tests case for create City")
public class CityServiceCreateTest {

    private static final int NO_INTERACTIONS = 0;

    @Autowired
    protected CityService service;

    @MockBean
    protected CityRepository repository;

    @Test
    public void create_shouldCallRepositoryToCreateANewCity() throws InstanceAlreadyExistsException {
        /* Given */
        final City expectedCity = new City(1L, "Gravatal", "SC");
        given(this.repository.save(any(City.class))).willReturn(expectedCity);

        /* When */
        final City createdCity = this.service.create(new City(null, "Gravatal", "SC"));

        /* Then */
        assertThat(createdCity).isEqualTo(expectedCity);
    }

    @Test
    public void create_shouldCallRepositoryAndThrowAnInstanceAlreadyExistsException() {
        given(this.repository.findByNameAndFederativeUnit(anyString(), anyString())).willReturn(
                Arrays.asList(new City(45L, "Porto Alegre", "RS"))
        );

        assertThrows(InstanceAlreadyExistsException.class, () -> this.service.create(new City(null, "Porto Alegre", "RS")));
        verify(this.repository, times(NO_INTERACTIONS)).save(any(City.class));
    }

}
