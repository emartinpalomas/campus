package com.example.campus.repository;

import com.example.campus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNationalIdAndCountry(String nationalId, String country);
    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();
}
