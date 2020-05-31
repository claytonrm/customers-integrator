package com.compasso.customersintegrator.controller;

import java.time.LocalDateTime;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.compasso.customersintegrator.model.City;
import com.compasso.customersintegrator.service.CityService;

@RestController
@RequestMapping("/v1/cities")
public class CityController {

    private final CityService service;

    @Autowired
    public CityController(final CityService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResourceCreatedResponse> create(@Valid @RequestBody final City newCity, final UriComponentsBuilder uriComponentsBuilder) throws InstanceAlreadyExistsException {
        final City createdCity = this.service.create(newCity);
        final UriComponents uriComponents = uriComponentsBuilder.cloneBuilder().path("/v1/cities/{id}").buildAndExpand(createdCity.getId());

        return ResponseEntity.created(uriComponents.toUri()).body(
                ResourceCreatedResponse.builder()
                        .id(createdCity.getId())
                        .uri(uriComponents.toUriString())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<City> get(@PathVariable final Long id) throws InstanceNotFoundException {
        return ResponseEntity.ok(this.service.findById(id));
    }

}
