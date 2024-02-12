package com.presidential.elections.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presidential.elections.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByusername(String username);
    Optional<User> findByuserid(Integer id);
}
