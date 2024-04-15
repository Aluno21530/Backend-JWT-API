package com.projects.loginauthapi.repositories;

import com.projects.loginauthapi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Interface que estende JpaRepository para realizar operações de acesso a dados relacionadas aos usuários
public interface UserRepository extends JpaRepository<User, String> {

    // Método para encontrar um usuário pelo email
    Optional<User> findByEmail(String email);
}
