package com.patasunidasapi.patasunidasapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_store")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageStore {

    @Id
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "base64_data", columnDefinition = "TEXT") 
    private String base64Data;
}