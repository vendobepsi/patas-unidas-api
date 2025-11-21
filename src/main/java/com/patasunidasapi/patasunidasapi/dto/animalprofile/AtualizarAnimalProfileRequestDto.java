// AtualizarAnimalProfileRequestDTO.java
package com.patasunidasapi.patasunidasapi.dto.animalprofile;

import com.patasunidasapi.patasunidasapi.model.AnimalSex;
import com.patasunidasapi.patasunidasapi.model.AnimalSize;
import com.patasunidasapi.patasunidasapi.model.AnimalStatus;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarAnimalProfileRequestDto {
    // Todos os campos são opcionais, pois o PATCH é parcial
    private String provisionalName;
    private String description;
    private String approximateAge;
    
    // Opcionais:
    private AnimalStatus status;
    private AnimalSize size;
    private AnimalSex sex;
    
    // Para GeoLocation:
    private Double latitude;
    private Double longitude;

    // Para fotos:
    private ArrayList<String> photos;
    
    // Não incluímos o ID nem os campos de criação (createdAt, createdByUserId)
}