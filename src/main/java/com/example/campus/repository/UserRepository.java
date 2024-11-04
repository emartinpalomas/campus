package com.example.campus.repository;

import com.example.campus.entity.NationalIdInfo;
import com.example.campus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNationalIdInfo(NationalIdInfo nationalIdInfo);
    @Query("SELECT u.username FROM User u")
    List<String> findAllUsernames();
}
