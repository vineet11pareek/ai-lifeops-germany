package com.lifeops.truthservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI truthServiceOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                .title("AI LifeOps Truth Service API")
                        .version("v1")
                        .description("API documentation for truth-service in AI Life Operations System — Germany Edition"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/vineet11pareek/ai-lifeops-germany"));
    }
}
