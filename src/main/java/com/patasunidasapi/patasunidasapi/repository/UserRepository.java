package com.patasunidasapi.patasunidasapi.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.patasunidasapi.patasunidasapi.model.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByName(String name);
    boolean existsByEmail(String email);
}
