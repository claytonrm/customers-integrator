package com.compasso.customersintegrator.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final CityService cityService;

    @Autowired
    public CityController(final CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResourceCreatedResponse> create(@RequestBody final City newCity, final UriComponentsBuilder uriComponentsBuilder) {
        final City createdCity = this.cityService.create(newCity);
        final UriComponents uriComponents = uriComponentsBuilder.cloneBuilder().path("/v1/cities/{id}").buildAndExpand(createdCity.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(
                ResourceCreatedResponse.builder()
                        .id(createdCity.getId())
                        .uri(uriComponents.toUriString())
                        .createdAt(LocalDateTime.now())
                        .build()
        );

    }

}
