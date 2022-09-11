package com.example.webapplication.WebConfiguration.RefreshToken;

import com.example.webapplication.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Override
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User userForDeletion);
}
