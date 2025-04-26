package com.example.demo.controller;

import com.example.demo.model.CityInfo;
import com.example.demo.model.TimeInfo;
import com.example.demo.services.CityService;
import com.example.demo.services.CityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private static final Logger logger = LoggerFactory.getLogger(CityController.class);
    private final CityService cityService;

    @GetMapping("/{name}")
    public CityInfo getCityByName(@PathVariable String name) {
        logger.info("getCityByName был вызван");
        return cityService.getCityByName(name);
    }

    @GetMapping
    public List<CityInfo> getAllCities() {
        logger.info("getAllCities был вызван");
        return cityService.getAllCities();
    }

    @GetMapping("/country/{countryName}")
    public List<CityInfo> getCityByCountry(@PathVariable String countryName) {
        logger.info("getCityByCountry был вызван");
        return cityService.getCitiesByCountry(countryName);
    }

    @GetMapping("/time/{name}")
    public TimeInfo getCityTimeInfoByName(@PathVariable String name) {
        logger.info("getCityTimeInfoByName был вызван");
        return cityService.getCityTimeInfoByName(name);
    }
}
