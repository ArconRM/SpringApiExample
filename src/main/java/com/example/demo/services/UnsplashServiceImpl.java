package com.example.demo.services;

import com.example.demo.services.interfaces.UnsplashService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Service
public class UnsplashServiceImpl implements UnsplashService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${unsplash.api.key}")
    private String apiKey;

    @Cacheable("cityImageUrls")
    @Override
    public String getCityImageUrl(String cityName) {
        try {
            String url = "https://api.unsplash.com/search/photos?query=" +
                    UriUtils.encode(cityName, StandardCharsets.UTF_8) +
                    "&page=1&per_page=1&client_id=" + apiKey;

            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, null, JsonNode.class);
            JsonNode results = response.getBody().get("results");
            if (results != null && results.isArray() && !results.isEmpty()) {
                return results.get(0).get("urls").get("small").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] fetchCityImage(String imageUrl) {
        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl, byte[].class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }
}
