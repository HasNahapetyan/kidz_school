package com.example.kidz_school.repository;

import com.example.kidz_school.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
//    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User findByEmail(String email);

}
