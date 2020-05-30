package com.compasso.customersintegrator.controller;

import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.compasso.customersintegrator.repository.CityRepository;
import com.compasso.customersintegrator.service.CityService;

@WebMvcTest(CityController.class)
public class CityControllerBase {

    protected static final String TARGET_RELATIVE_PATH = "/v1/cities";

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected CityService service;

    @MockBean
    private CityRepository repository;

    @AfterEach
    public void tearDown() {
        Mockito.reset(this.service, this.repository);
    }

}
