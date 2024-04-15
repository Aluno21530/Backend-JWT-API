package com.projects.loginauthapi.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    SecurityFilter securityFilter;

    // Define a configuração de segurança para o Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita a proteção CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define a política de gerenciamento de sessão como stateless
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Permite acesso sem autenticação à rota /auth/login via método POST
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Permite acesso sem autenticação à rota /auth/register via método POST
                        .anyRequest().authenticated() // Todas as outras solicitações exigem autenticação
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona um filtro personalizado antes do filtro padrão UsernamePasswordAuthenticationFilter
        return http.build(); // Constrói e retorna a cadeia de filtros de segurança configurada
    }

    // Define um bean para o PasswordEncoder, que é usado para codificar e verificar senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Retorna um codificador de senha BCrypt
    }

    // Define um bean para o AuthenticationManager, que é usado para autenticar solicitações de login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Retorna o gerenciador de autenticação configurado
    }
}
