package com.e_commerce.service;

import com.e_commerce.dto.CityDto;
import com.e_commerce.exception.ResourceNotFoundException;

import java.util.List;

public interface CityService {
    List<CityDto> getAllCities();
    CityDto getCityById(Long id) throws ResourceNotFoundException;
    CityDto createCity(CityDto cityDto);
    CityDto updateCity(Long id, CityDto cityDto) throws ResourceNotFoundException;
    void deleteCity(Long id) throws ResourceNotFoundException;
}