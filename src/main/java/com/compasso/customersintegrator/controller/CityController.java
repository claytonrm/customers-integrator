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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.compasso.customersintegrator.domain.CityCriteria;
import com.compasso.customersintegrator.domain.ResourceCreatedResponse;
import com.compasso.customersintegrator.domain.dto.CityDTO;
import com.compasso.customersintegrator.domain.model.City;
import com.compasso.customersintegrator.service.CityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Cities", value = "Operations for city resources")
@RestController
@RequestMapping("/v1/cities")
public class CityController {

    private final CityService service;
    private final ModelMapper modelMapper;

    @Autowired
    public CityController(final CityService service, final ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(value = "Create a single city")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResourceCreatedResponse> create(@Valid @RequestBody final CityDTO newCity, final UriComponentsBuilder uriComponentsBuilder) throws InstanceAlreadyExistsException {
        final CityDTO createdCity = toDTO(this.service.create(toEntity(newCity)));
        final UriComponents uriComponents = uriComponentsBuilder.cloneBuilder().path("/v1/cities/{id}").buildAndExpand(createdCity.getId());

        return ResponseEntity.created(uriComponents.toUri()).body(
                ResourceCreatedResponse.builder()
                        .id(createdCity.getId())
                        .uri(uriComponents.toUriString())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @ApiOperation(value = "Get a single city")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CityDTO> get(@PathVariable final Long id) throws InstanceNotFoundException {
        return ResponseEntity.ok(toDTO(this.service.findById(id)));
    }

    @ApiOperation(value = "Get cities by query params")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CityDTO>> findByParams(final CityCriteria criteria) {
        return ResponseEntity.ok(
                this.service.findByCriteria(criteria)
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList())
        );
    }

    private CityDTO toDTO(final City city) {
        return modelMapper.map(city, CityDTO.class);
    }

    private City toEntity(final CityDTO cityDTO) {
        return modelMapper.map(cityDTO, City.class);
    }

}
