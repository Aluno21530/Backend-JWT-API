package com.projects.loginauthapi.infra.security;

import com.projects.loginauthapi.domain.user.User;
import com.projects.loginauthapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Procura o usuário no banco de dados pelo nome de usuário (email, neste caso)
        User user = this.repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Retorna os detalhes do usuário para o Spring Security.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                null //lista de authorities
        );
    }
}
