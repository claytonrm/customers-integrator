package com.compasso.customersintegrator.controller;

import java.time.LocalDateTime;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.compasso.customersintegrator.domain.ResourceCreatedResponse;
import com.compasso.customersintegrator.model.Customer;
import com.compasso.customersintegrator.service.CustomerService;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerService service;

    @Autowired
    private CustomerController(final CustomerService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResourceCreatedResponse> create(@Valid @RequestBody final Customer newCustomer, final UriComponentsBuilder uriComponentsBuilder)
            throws InstanceAlreadyExistsException {
        final Customer createdCustomer = this.service.create(newCustomer);
        final UriComponents uriComponents = uriComponentsBuilder.cloneBuilder().path("/v1/customers/{id}").buildAndExpand(createdCustomer.getId());

        return ResponseEntity.created(uriComponents.toUri()).body(
                ResourceCreatedResponse.builder()
                        .id(createdCustomer.getId())
                        .uri(uriComponents.toUriString())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Customer> get(@PathVariable final Long id) throws InstanceNotFoundException {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> delete(@PathVariable final Long id) throws InstanceNotFoundException {
        this.service.remove(id);
        return ResponseEntity.noContent().build();
    }

}
