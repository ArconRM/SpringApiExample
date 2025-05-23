package com.example.demo.controller;

import com.example.demo.exceptions.CityNotFoundException;
import com.example.demo.exceptions.IncorrectInputException;
import com.example.demo.model.CityInfo;
import com.example.demo.services.interfaces.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CityViewController {
    private final CityService cityService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        return "index";
    }

    @GetMapping("/cities")
    public String getAllCities(Model model) {
        List<CityInfo> results = cityService.getAllCities();
        model.addAttribute("cities", results);
        return "index";
    }

    @GetMapping("/city/{name}")
    public String cityDetails(@PathVariable String name, Model model) throws CityNotFoundException {
        CityInfo city = cityService.getCityByName(name);
        if (city == null) throw new CityNotFoundException();
        model.addAttribute("city", city);
        return "city";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model) {
        try {
            List<CityInfo> results = cityService.searchCities(query);
            model.addAttribute("cities", results);
        } catch (IncorrectInputException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "index";
    }

    @ExceptionHandler(CityNotFoundException.class)
    public String handleError() {
        return "error";
    }
}
