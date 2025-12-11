package com.patasunidasapi.patasunidasapi.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.patasunidasapi.patasunidasapi.model.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByName(String name);
    boolean existsByEmail(String email);

    @Modifying
    @Transactional // Obrigatório para updates/deletes customizados
    @Query("UPDATE User u SET u.profilePictureUrl = :url WHERE u.id = :id")
    void updatePhotoUrl(Long id, String url);
}
