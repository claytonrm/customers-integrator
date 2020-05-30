package com.compasso.customersintegrator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compasso.customersintegrator.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
