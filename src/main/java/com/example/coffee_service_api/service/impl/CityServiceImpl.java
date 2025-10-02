package com.example.coffee_service_api.service.impl;

import com.example.coffee_service_api.dto.CityDto;
import com.example.coffee_service_api.model.City;
import com.example.coffee_service_api.repo.CityRepository;
import com.example.coffee_service_api.service.abs.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<CityDto> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityDto getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));
        return toDto(city);
    }

    @Override
    public CityDto createCity(CityDto cityDto) {
        City city = new City();
        city.setName(cityDto.getName());
        return toDto(cityRepository.save(city));
    }

    @Override
    public CityDto updateCity(Long id, CityDto cityDto) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));
        city.setName(cityDto.getName());
        return toDto(cityRepository.save(city));
    }

    @Override
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }

    private CityDto toDto(City city) {
        return new CityDto(city.getId(), city.getName());
    }
}
