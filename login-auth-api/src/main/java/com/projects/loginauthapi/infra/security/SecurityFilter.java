package com.projects.loginauthapi.infra.security;

import com.projects.loginauthapi.domain.user.User;
import com.projects.loginauthapi.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    // Este método é chamado para executar a lógica de filtragem em cada solicitação
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera o token do cabeçalho da solicitação
        var token = this.recoverToken(request);

        // Valida o token e obtém o login (email) do usuário, se o token for válido
        var login = tokenService.validateToken(token);

        // Se o login não for nulo, o token é válido e podemos autenticar o usuário
        if (login != null) {
            // Procura o usuário no banco de dados pelo email
            User user = userRepository.findByEmail(login)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Define as autorizações do usuário (neste exemplo, apenas uma ROLE_USER)
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            // Cria um objeto de autenticação com o usuário, autorizações e sem credenciais (senha)
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            // Define a autenticação no contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    // Método para recuperar o token do cabeçalho "Authorization" da solicitação
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", ""); // Retorna apenas o token, removendo o prefixo "Bearer "
    }
}
