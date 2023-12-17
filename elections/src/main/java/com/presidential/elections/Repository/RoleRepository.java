package com.presidential.elections.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presidential.elections.Entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findById(Integer role_id);
    Optional<Role> findByAuthority(String authority);
}
