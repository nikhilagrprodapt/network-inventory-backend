package com.company.network_inventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI networkInventoryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Network Inventory API")
                        .description("Backend APIs for Headend, FDH, Splitter, Customers, Assets, Technicians, Tasks, and Audit Logs.")
                        .version("1.0.0"));
    }
}
