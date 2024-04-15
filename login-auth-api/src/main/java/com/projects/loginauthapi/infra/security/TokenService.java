package com.projects.loginauthapi.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.projects.loginauthapi.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //Injeta a chave secreta para assinar os tokens JWT
    @Value("${api.security.token.key}")
    private String key;

    // Método para gerar um token JWT para o usuário
    public String generateToken(User user) {
        try {
            // Cria um algoritmo de assinatura HMAC com a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(key);

            // Cria o token JWT com o emissor, assunto, data de expiração e assina com o algoritmo
            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            // Se houver uma exceção ao criar o token JWT, lança uma RuntimeException
            throw new RuntimeException("JWT generation failed", exception);
        }
    }

    // Método para validar e extrair o email do usuário a partir do token JWT
    public String validateToken(String token) {
        try {
            // Cria um algoritmo de verificação HMAC com a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(key);

            // Verifica o token JWT e extrai o email do assunto
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            // Se houver uma exceção ao verificar o token JWT, retorna null
            return null;
        }
    }

    // Método para gerar a data de expiração do token (2 horas a partir do momento atual)
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC);
    }
}
