package com.lifeops.aiservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI aiServiceOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("AI Lifeops AI Service API")
                        .version("v1")
                        .description("API documentation for ai-service in AI Life Operations System — Germany Edition"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/vineet11pareek/ai-lifeops-germany"));
    }
}
