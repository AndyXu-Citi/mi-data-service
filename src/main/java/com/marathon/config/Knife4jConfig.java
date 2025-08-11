package com.marathon.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j配置
 *
 * @author marathon
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("马拉松报名系统API文档")
                        .description("马拉松报名系统后端服务接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("marathon")
                                .email("marathon@example.com"))
                        .termsOfService("http://localhost:8080/"));
    }
}