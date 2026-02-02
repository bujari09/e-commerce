package com.e_commerce.service.impl;

import com.e_commerce.dto.CityDto;
import com.e_commerce.entity.City;
import com.e_commerce.exception.ResourceNotFoundException;
import com.e_commerce.repository.CityRepository;
import com.e_commerce.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<CityDto> getAllCities() {
        List<City> cities = cityRepository.findAll();
        List<CityDto> cityDtos = new ArrayList<>();

        for (City city : cities) {
            cityDtos.add(convertToDto(city));
        }

        return cityDtos;
    }

    @Override
    public CityDto getCityById(Long id) throws ResourceNotFoundException {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        return convertToDto(city);
    }

    @Override
    public CityDto createCity(CityDto cityDto) {
        City city = new City();
        city.setName(cityDto.getName());

        City savedCity = cityRepository.save(city);
        return convertToDto(savedCity);
    }

    @Override
    public CityDto updateCity(Long id, CityDto cityDto) throws ResourceNotFoundException {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));

        city.setName(cityDto.getName());

        City updatedCity = cityRepository.save(city);
        return convertToDto(updatedCity);
    }

    @Override
    public void deleteCity(Long id) throws ResourceNotFoundException {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City not found with id: " + id);
        }
        cityRepository.deleteById(id);
    }

    private CityDto convertToDto(City city) {
        CityDto dto = new CityDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        return dto;
    }
}