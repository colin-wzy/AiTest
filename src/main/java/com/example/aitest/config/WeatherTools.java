package com.example.aitest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class WeatherTools {

    @Bean
    @Description("获取天气情况")
    public Function<String, String> getWeather() {
        return location -> "{\"location\": \"" + location + "\", \"temperature\": \"25℃\", \"condition\": \"晴朗\"}";
    }
}
