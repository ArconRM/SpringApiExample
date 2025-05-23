package com.example.demo.services;

import com.example.demo.exceptions.IncorrectInputException;
import com.example.demo.model.CityInfo;
import com.example.demo.model.TimeInfo;
import com.example.demo.services.interfaces.CityService;
import com.example.demo.services.interfaces.UnsplashService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final UnsplashService unsplashService;

    private final List<CityInfo> cities = new ArrayList<>();

    @PostConstruct
    public void init() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass()
                                .getClassLoader()
                                .getResourceAsStream("cities.csv")),
                        StandardCharsets.UTF_8))) {
            String line;
            reader.readLine(); // пропустить заголовок
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    cities.add(new CityInfo(
                            parts[0],
                            parts[1],
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            new TimeInfo(parts[4], null, null, null),
                            Long.parseLong(parts[5]),
                            null,
                            new byte[]{}
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CityInfo getCityByName(String name) {
        return cities.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .map(this::enrichCityWithTime)
                .map(this::enrichCityWithImage)
                .findFirst()
                .orElse(null);
    }

    public List<CityInfo> getAllCities() {
        return enrichCitiesWithTime(cities);
    }

    @Override
    public List<CityInfo> getCitiesByCountry(String country) {
        return cities.stream()
                .filter(c -> c.getCountry().equalsIgnoreCase(country))
                .map(this::enrichCityWithTime)
                .toList();
    }

    @Override
    public TimeInfo getCityTimeInfoByName(String name) {
        return cities.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .map(this::enrichCityWithTime)
                .findFirst()
                .map(CityInfo::getTimeInfo)
                .orElse(null);
    }

    @Override
    public List<CityInfo> searchCities(String query) throws IncorrectInputException {
        if (query.isEmpty()) {
            return cities.stream()
                    .map(this::enrichCityWithTime)
                    .toList();
        }
        if (!isValidQuery(query)) {
            throw new IncorrectInputException("Некорректный запрос, попробуйте еще разок");
        }

        return cities.stream()
                .filter(c ->
                        c.getName().contains(query) || c.getCountry().contains(query)
                )
                .map(this::enrichCityWithTime)
                .toList();
    }

    private boolean isValidQuery(String query) {
        String regex = "^[\\p{L}][\\p{L} .'-]{0,98}[\\p{L}]$";
        return query != null && query.matches(regex);
    }

    private List<CityInfo> enrichCitiesWithTime(List<CityInfo> cityList) {
        List<CityInfo> updated = new ArrayList<>();
        for (CityInfo city : cityList) {
            updated.add(enrichCityWithTime(city));
        }
        return updated;
    }

    private CityInfo enrichCityWithTime(CityInfo city) {
        TimeInfo timeInfo = city.getTimeInfo();
        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timeInfo.getTimezone()));
            timeInfo.setLocalTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            timeInfo.setUtcTime(Instant.now().toString()); // UFC-формат (ISO 8601, UTC)
        } catch (Exception e) {
            timeInfo.setLocalTime("Unknown");
            timeInfo.setUtcTime("Unknown");
        }
        return city;
    }

    private CityInfo enrichCityWithImage(CityInfo city) {
        if (city.getImageUrl() == null || city.getImageUrl().isEmpty()) {
            String imageUrl = unsplashService.getCityImageUrl(city.getName());
            if (imageUrl != null) {
                city.setImageUrl(imageUrl);
            }
        }
        if (city.getImageUrl() != null && city.getImageData().length == 0) {
            byte[] image = unsplashService.fetchCityImage(city.getImageUrl());
            city.setImageData(image);
        }
        return city;
    }

}