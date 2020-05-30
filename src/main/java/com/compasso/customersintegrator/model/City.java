package com.compasso.customersintegrator.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Property \"name\" is required.")
    private String name;

    @NotBlank(message = "Property \"federativeUnit\" is required.")
    private String federativeUnit;

}
