package com.compasso.customersintegrator.domain.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.compasso.customersintegrator.domain.Gender;
import com.compasso.customersintegrator.domain.model.City;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {

    private Long id;

    @NotBlank(message = "Property \"name\" is required.")
    private String name;

    @NotNull(message = "Property \"gender\" is required.")
    private Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull(message = "Property \"birthdate\" is required.")
    private LocalDate birthdate;

    @NotNull(message = "Property \"age\" is required.")
    private Integer age;

    @EqualsAndHashCode.Exclude
    private City city;

}
