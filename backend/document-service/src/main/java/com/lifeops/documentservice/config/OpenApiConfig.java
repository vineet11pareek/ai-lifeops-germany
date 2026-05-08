package com.lifeops.documentservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI documentServiceOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("AI LifeOps Document Service API")
                        .version("v1")
                        .description("API documentation for document-service in AI Life Operations System — Germany Edition"))
                .externalDocs(new ExternalDocumentation()
                        .url("https://github.com/vineet11pareek/ai-lifeops-germany")
                        .description("Project Documentation"));

    }
}
