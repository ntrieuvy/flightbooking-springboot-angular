package com.bm.travelcore.repository;

import com.bm.travelcore.model.User;
import com.bm.travelcore.model.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findFirstByRoles_Name(String name);
}
