package com.example.webapplication.Administrator;

import com.example.webapplication.User.User;
import com.example.webapplication.User.UserCompositePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Administrator, Long> {

    @Query("SELECT u FROM User u WHERE u.isRegistered= false")
    List<User> showRegistrationRequests();

    Boolean existsByEmail(String email);
    Optional<Administrator> findByEmail(String email);
}