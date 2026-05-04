package com.lifeops.userservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("AI LifeOps User Service API")
                        .version("v1")
                        .description("API documentation for user-service in AI Life Operation System"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project documentation")
                        .url("https://github.com/vineet11pareek/ai-lifeops-germany"));
    }
}
