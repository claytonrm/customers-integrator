package com.compasso.customersintegrator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.compasso.customersintegrator.repository.CityRepository;

@SpringBootTest
public class CityServiceBase {

    @Autowired
    protected CityService service;

    @MockBean
    protected CityRepository repository;

}
