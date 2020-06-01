package com.compasso.customersintegrator.domain.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityDTO {

    private Long id;

    @NotBlank(message = "Property \"name\" is required.")
    private String name;

    @NotBlank(message = "Property \"federativeUnit\" is required.")
    private String federativeUnit;

}
