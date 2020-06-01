package com.compasso.customersintegrator.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.compasso.customersintegrator.domain.CustomerCriteria;
import com.compasso.customersintegrator.domain.ResourceCreatedResponse;
import com.compasso.customersintegrator.domain.dto.CustomerDTO;
import com.compasso.customersintegrator.domain.model.Customer;
import com.compasso.customersintegrator.service.CustomerService;
import com.github.fge.jsonpatch.JsonPatch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Customers", value = "Operations for customers resources")
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerService service;
    private final ModelMapper modelMapper;

    @Autowired
    private CustomerController(final CustomerService service, final ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(value = "Create a single customer")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResourceCreatedResponse> create(@Valid @RequestBody final CustomerDTO newCustomer, final UriComponentsBuilder uriComponentsBuilder)
            throws InstanceAlreadyExistsException {
        final CustomerDTO createdCustomer = toDTO(this.service.create(toEntity(newCustomer)));
        final UriComponents uriComponents = uriComponentsBuilder.cloneBuilder().path("/v1/customers/{id}").buildAndExpand(createdCustomer.getId());

        return ResponseEntity.created(uriComponents.toUri()).body(
                ResourceCreatedResponse.builder()
                        .id(createdCustomer.getId())
                        .uri(uriComponents.toUriString())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @ApiOperation(value = "Get a single customer")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CustomerDTO> get(@PathVariable final Long id) throws InstanceNotFoundException {
        return ResponseEntity.ok(toDTO(this.service.findById(id)));
    }

    @ApiOperation(value = "Get customers by query params")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CustomerDTO>> findByParams(final CustomerCriteria criteria) {
        return ResponseEntity.ok(
                this.service.findByCriteria(criteria)
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList())
        );
    }

    @ApiOperation(value = "Remove a single customer")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> delete(@PathVariable final Long id) throws InstanceNotFoundException {
        this.service.remove(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Partial update on customer")
    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CustomerDTO> update(@PathVariable final Long id, @RequestBody JsonPatch patch) throws InstanceNotFoundException {
        return ResponseEntity.ok(toDTO(this.service.update(id, patch)));
    }

    @ApiOperation(value = "Full update on customer")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> update(@PathVariable final Long id, @RequestBody CustomerDTO customer) throws InstanceNotFoundException {
        this.service.update(id, toEntity(customer));
        return ResponseEntity.noContent().build();
    }

    private CustomerDTO toDTO(final Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    private Customer toEntity(final CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

}
