package com.example.demo.services.interfaces;

public interface UnsplashService {
    String getCityImageUrl(String cityName);
    byte[] fetchCityImage(String imageUrl);
}
