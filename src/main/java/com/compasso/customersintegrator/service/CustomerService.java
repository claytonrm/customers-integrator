package com.compasso.customersintegrator.service;

import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.compasso.customersintegrator.domain.CustomerCriteria;
import com.compasso.customersintegrator.domain.model.City;
import com.compasso.customersintegrator.domain.model.Customer;
import com.compasso.customersintegrator.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final CityService cityService;
    private final ObjectMapper mapper;

    @Autowired
    private CustomerService(final CustomerRepository repository, final CityService cityService) {
        this.repository = repository;
        this.cityService = cityService;
        this.mapper = new ObjectMapper();
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

    public Customer findById(final Long id) throws InstanceNotFoundException {
        if (id == null) {
            return null;
        }
        return this.repository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException(String.format("Customer %d does not exist!", id)));
    }

    public void remove(final Long id) throws InstanceNotFoundException {
        final Customer existingCustomer = findById(id);
        this.repository.delete(existingCustomer);
    }

    public List<Customer> findByCriteria(final CustomerCriteria criteria) {
        if (criteria != null && !StringUtils.isEmpty(criteria.getName())) {
            return this.repository.findByName(criteria.getName());
        }
        return this.repository.findAll();
    }

    private City getExistingCustomerCity(final Long cityId) {
        try {
            return this.cityService.findById(cityId);
        } catch (InstanceNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Customer update(final Long id, final JsonPatch patch) throws InstanceNotFoundException {
        final Customer existingCustomer = findById(id);
        return this.repository.save(applyPatchToCustomer(patch, existingCustomer));
    }

    public void update(final Long id, final Customer customer) throws InstanceNotFoundException {
        this.findById(id);
        this.repository.save(customer);
    }

    private Customer applyPatchToCustomer(final JsonPatch patch, final Customer targetCustomer) {
        try {
            final JsonNode patched = patch.apply(mapper.convertValue(targetCustomer, JsonNode.class));
            return mapper.treeToValue(patched, Customer.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new IllegalArgumentException("Could not process custom update operation", e);
        }
    }
}
