package com.patasunidasapi.patasunidasapi.repository;

import com.patasunidasapi.patasunidasapi.model.ImageStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageStoreRepository extends JpaRepository<ImageStore, String> {
    
}