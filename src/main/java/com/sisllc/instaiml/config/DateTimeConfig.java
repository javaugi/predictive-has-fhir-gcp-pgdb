/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.config;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class DateTimeConfig implements WebFluxConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalDateToOffsetDateTimeConverter());
        registry.addConverter(new OffsetDateTimeToLocalDateConverter());
    }

    @WritingConverter
    public static class LocalDateToOffsetDateTimeConverter implements Converter<LocalDate, OffsetDateTime> {
        @Override
        public OffsetDateTime convert(LocalDate source) {
            return source.atStartOfDay().atOffset(ZoneOffset.UTC);
        }
    }

    @ReadingConverter
    public static class OffsetDateTimeToLocalDateConverter implements Converter<OffsetDateTime, LocalDate> {
        @Override
        public LocalDate convert(OffsetDateTime source) {
            return source.toLocalDate();
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("https://example.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
    }
}
