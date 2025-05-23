package com.example.demo.services.interfaces;

import com.example.demo.exceptions.IncorrectInputException;
import com.example.demo.model.CityInfo;
import com.example.demo.model.TimeInfo;

import java.util.List;

public interface CityService {
    CityInfo getCityByName(String name);
    List<CityInfo> getAllCities();
    List<CityInfo> getCitiesByCountry(String country);
    TimeInfo getCityTimeInfoByName(String name);

    List<CityInfo> searchCities(String query) throws IncorrectInputException;
}
