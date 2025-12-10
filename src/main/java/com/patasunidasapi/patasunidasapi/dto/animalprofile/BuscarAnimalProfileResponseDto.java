package com.patasunidasapi.patasunidasapi.dto.animalprofile;

import java.util.ArrayList;


import com.patasunidasapi.patasunidasapi.model.AnimalSex;
import com.patasunidasapi.patasunidasapi.model.AnimalSize;
import com.patasunidasapi.patasunidasapi.model.AnimalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuscarAnimalProfileResponseDto {
    private Long id;
    private Long createdByUserId;
    private Long managedByUserId;
    private ArrayList<String> photos;
    private Double latitude;
    private Double longitude;
    private AnimalStatus status;
    private Long createdAt;

    private String provisionalName;
    private String description;
    private AnimalSize size;
    private AnimalSex sex;
    private String approximateAge;
    private Double approximateDistance;

    
}
