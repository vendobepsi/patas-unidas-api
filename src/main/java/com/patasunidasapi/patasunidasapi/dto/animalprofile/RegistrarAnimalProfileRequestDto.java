package com.patasunidasapi.patasunidasapi.dto.animalprofile;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.patasunidasapi.patasunidasapi.model.AnimalSex;
import com.patasunidasapi.patasunidasapi.model.AnimalSize;
import com.patasunidasapi.patasunidasapi.model.AnimalStatus;

import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrarAnimalProfileRequestDto {
    private ArrayList<String> photos;
    @Embedded

    private Long createdByUserId;

    private Double latitude;
    private Double longitude;
    private AnimalStatus status;
    private Long createdAt;
    private LocalDateTime createdAtLocalDateTime;
    private String provisionalName;
    private String description;
    private AnimalSize size;
    private AnimalSex sex;
    private String approximateAge;
}
