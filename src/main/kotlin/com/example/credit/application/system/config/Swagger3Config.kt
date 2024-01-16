package com.example.credit.application.system.config

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Config {

    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
                .group("springcreditapplicationsystem-public")
                .pathsToMatch("/api/customer/**", "/api/credit/**")
                .build()
    }
}