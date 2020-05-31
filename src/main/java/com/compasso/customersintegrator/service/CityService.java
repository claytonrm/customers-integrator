package com.compasso.customersintegrator.service;

import java.util.Arrays;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.repository.CityRepository;

@Service
public class CityService {

    private CityRepository repository;

    @Autowired
    private CityService(final CityRepository repository) {
        this.repository = repository;
    }

    public City create(final City newCity) throws InstanceAlreadyExistsException {
        final List<City> existingCities = findByNameAndFederativeUnit(newCity.getName(), newCity.getFederativeUnit());
        if (!existingCities.isEmpty()) {
            final Long existingCityId = existingCities.stream().findFirst().get().getId();
            throw new InstanceAlreadyExistsException(String.format("City already exists! You may check it on City ID: [%d].", existingCityId));
        }

        return this.repository.save(newCity);
    }

    public City findById(final Long id) throws InstanceNotFoundException {
        if (id == null) {
            return null;
        }
        return this.repository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException(String.format("City %d does not exist!", id)));
    }

    public List<City> findByNameAndFederativeUnit(final String name, final String federativeUnit) {
        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(federativeUnit)) {
            return this.repository.findByNameAndFederativeUnit(name, federativeUnit);
        }

        return Arrays.asList();
    }

}
