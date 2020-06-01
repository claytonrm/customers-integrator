package com.compasso.customersintegrator.service;

import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.compasso.customersintegrator.domain.CityCriteria;
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
        final List<City> existingCities = findByCriteria(CityCriteria.builder()
                .name(newCity.getName())
                .federativeUnit(newCity.getFederativeUnit())
                .build()
        );
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

    /* Change approach in case of increasing number of fields on CityCriteria */
    public List<City> findByCriteria(final CityCriteria criteria) {
        if (criteria != null) {
            if (!StringUtils.isEmpty(criteria.getName()) && !StringUtils.isEmpty(criteria.getFederativeUnit())) {
                return this.repository.findByNameAndFederativeUnit(criteria.getName(), criteria.getFederativeUnit());
            }

            if (!StringUtils.isEmpty(criteria.getName())) {
                return this.repository.findByName(criteria.getName());
            }

            if (!StringUtils.isEmpty(criteria.getFederativeUnit())) {
                return this.repository.findByFederativeUnit(criteria.getName());
            }
        }
        return this.repository.findAll();
    }

}
