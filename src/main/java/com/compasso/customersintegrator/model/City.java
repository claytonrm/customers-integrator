package com.compasso.customersintegrator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    private String id;

    private String name;
    private String federativeUnit;

}
