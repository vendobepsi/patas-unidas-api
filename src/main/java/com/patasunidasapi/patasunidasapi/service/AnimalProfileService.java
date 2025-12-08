package com.patasunidasapi.patasunidasapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.patasunidasapi.patasunidasapi.Utility.DateTimeConverter;
import com.patasunidasapi.patasunidasapi.Utility.ImageConverter;
import com.patasunidasapi.patasunidasapi.dto.animalprofile.AtualizarAnimalProfileRequestDto;
import com.patasunidasapi.patasunidasapi.dto.animalprofile.BuscarAnimalProfileResponseDto;
import com.patasunidasapi.patasunidasapi.dto.animalprofile.RegistrarAnimalProfileRequestDto;
import com.patasunidasapi.patasunidasapi.model.AnimalProfile;
import com.patasunidasapi.patasunidasapi.model.GeoLocation;
import com.patasunidasapi.patasunidasapi.repository.AnimalProfileRepository;

import jakarta.transaction.Transactional;

@Service
public class AnimalProfileService {
    private final AnimalProfileRepository animalProfileRepository;

    public AnimalProfileService(AnimalProfileRepository animalProfileRepository) {
        this.animalProfileRepository = animalProfileRepository;
    }

    public List<AnimalProfile> findAllProfiles() {
        return animalProfileRepository.findAll();
    }
    public List<AnimalProfile> findAllSortedByCreatedAt(Sort.Direction direction){

        Sort sort = Sort.by(direction, "createdAt");

        return animalProfileRepository.findAll(sort);
    }

    public List<AnimalProfile> findAllNewestFirst() {
        return findAllSortedByCreatedAt(Sort.Direction.DESC);
    }

    public List<AnimalProfile> findAllOldestFirst() {
        return findAllSortedByCreatedAt(Sort.Direction.ASC);
    }
    
    //convert de registro
    public AnimalProfile convertToEntity(RegistrarAnimalProfileRequestDto dto){
        AnimalProfile animalProfile = new AnimalProfile();
        animalProfile.setCreatedByUserId(dto.getCreatedByUserId());
        animalProfile.setManagedByUserId(dto.getCreatedByUserId());
        animalProfile.setApproximateAge(dto.getApproximateAge());
        animalProfile.setCreatedAt(dto.getCreatedAt());
        animalProfile.setDescription(dto.getDescription());
        animalProfile.setLocation(new GeoLocation(dto.getLatitude(), dto.getLongitude()));
        ArrayList<String> imagepaths = dto.getPhotos();
        for (String string : imagepaths) {
            animalProfile.addPhoto(string);
        }
        animalProfile.setProvisionalName(dto.getProvisionalName());
        animalProfile.setSex(dto.getSex());
        animalProfile.setSize(dto.getSize());
        animalProfile.setStatus(dto.getStatus());

        return animalProfile;
    }

    //convert de response para busca
    public BuscarAnimalProfileResponseDto convertToDto(AnimalProfile animal){
        BuscarAnimalProfileResponseDto dto = new BuscarAnimalProfileResponseDto(animal.getId(), animal.getCreatedByUserId(), animal.getManagedByUserId(),
        animal.getPhotos(), animal.getLocation().getLatitude(), animal.getLocation().getLongitude(), animal.getStatus(), animal.getCreatedAt(),
         animal.getProvisionalName(), animal.getDescription(), animal.getSize(), animal.getSex(), animal.getApproximateAge());

         return dto;
    }

    @Transactional
    public Boolean registerAnimalProfile(AnimalProfile animalProfile){
        if (animalProfile == null) {
            return false;
        }
        animalProfileRepository.save(animalProfile);
        return true;
    }

    @Transactional
    public AnimalProfile updatProfile(Long profileId, AtualizarAnimalProfileRequestDto animalDto){

        AnimalProfile animal = animalProfileRepository.findById(profileId).orElseThrow(() -> new NoSuchElementException("Perfil não encontrado com esse ID: " + profileId));
        if(animalDto.getProvisionalName() != null)
            animal.setProvisionalName(animalDto.getProvisionalName());
        if(animalDto.getDescription() != null)
            animal.setDescription(animalDto.getDescription());
        if(animalDto.getStatus() != null)
            animal.setStatus(animalDto.getStatus());

        if (animalDto.getLatitude() != null && animalDto.getLongitude() != null) 
            animal.setLocation(new GeoLocation(animalDto.getLatitude(), animalDto.getLongitude()));
        
        return animalProfileRepository.save(animal);
    }
}
