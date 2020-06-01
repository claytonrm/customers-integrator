package com.compasso.customersintegrator.controller;

import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.compasso.customersintegrator.domain.dto.CustomerDTO;
import com.compasso.customersintegrator.domain.model.Customer;
import com.compasso.customersintegrator.repository.CityRepository;
import com.compasso.customersintegrator.repository.CustomerRepository;
import com.compasso.customersintegrator.service.CityService;
import com.compasso.customersintegrator.service.CustomerService;

public class ControllerBase {

    protected static final String CITY_RELATIVE_PATH = "/v1/cities/";
    protected static final String CUSTOMER_RELATIVE_PATH = "/v1/customers/";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    protected CityService cityService;

    @MockBean
    protected CustomerService customerService;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private CustomerRepository customerRepository;

    protected CustomerDTO toDTO(final Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    protected Customer toEntity(final CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(this.cityService, this.customerService, this.cityRepository, this.customerRepository);
    }

}
