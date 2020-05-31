package com.compasso.customersintegrator.service;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.model.Customer;
import com.compasso.customersintegrator.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CityService cityService;

    @Autowired
    private CustomerService(final CustomerRepository repository, final CityService cityService) {
        this.repository = repository;
        this.cityService = cityService;
    }

    public Customer create(final Customer customer) throws InstanceAlreadyExistsException {
        if (customer.getId() != null) {
            throw new IllegalArgumentException("Field \"id\" must not be provided. It will be generated automatically.");
        }

        final City customerCity = customer.getCity();
        if (customerCity != null && getExistingCustomerCity(customerCity.getId()) == null) {
            this.cityService.create(customerCity);
        }
        return this.repository.save(customer);
    }

    private City getExistingCustomerCity(final Long cityId) {
        try {
            return this.cityService.findById(cityId);
        } catch (InstanceNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}