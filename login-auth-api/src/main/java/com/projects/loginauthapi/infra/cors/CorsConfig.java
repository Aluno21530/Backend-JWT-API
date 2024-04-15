package com.projects.loginauthapi.infra.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    //Classe para habilitar requisições vindas do domínio declarado
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //dom+inio onde esta hospedado o frontend
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "DELETE", "PUT");
    }
}
