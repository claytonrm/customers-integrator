package com.compasso.customersintegrator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.repository.CityRepository;

@Service
public class CityService {

    private CityRepository repository;

    @Autowired
    private CityService(final CityRepository repository) {
        this.repository = repository;
    }

    public City create(final City newCity) {
        return this.repository.save(newCity);
    }

}
