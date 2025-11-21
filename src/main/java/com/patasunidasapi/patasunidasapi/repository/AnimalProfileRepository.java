package com.patasunidasapi.patasunidasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patasunidasapi.patasunidasapi.model.AnimalProfile;


public interface AnimalProfileRepository extends JpaRepository<AnimalProfile, Long> {
}
