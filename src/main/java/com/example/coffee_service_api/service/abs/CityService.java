package com.example.coffee_service_api.service.abs;

import com.example.coffee_service_api.dto.CityDto;

import java.util.List;

public interface CityService {
    List<CityDto> getAllCities();
    CityDto getCityById(Long id);
    CityDto createCity(CityDto cityDto);
    CityDto updateCity(Long id, CityDto cityDto);
    void deleteCity(Long id);
}
