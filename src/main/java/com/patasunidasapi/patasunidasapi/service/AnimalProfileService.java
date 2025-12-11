package com.patasunidasapi.patasunidasapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.patasunidasapi.patasunidasapi.Utility.ImageConverter;
import com.patasunidasapi.patasunidasapi.dto.animalprofile.AtualizarAnimalProfileRequestDto;
import com.patasunidasapi.patasunidasapi.dto.animalprofile.BuscarAnimalProfileResponseDto;
import com.patasunidasapi.patasunidasapi.dto.animalprofile.RegistrarAnimalProfileRequestDto;
import com.patasunidasapi.patasunidasapi.model.AnimalProfile;
import com.patasunidasapi.patasunidasapi.model.GeoLocation;
import com.patasunidasapi.patasunidasapi.repository.AnimalProfileRepository;
import com.patasunidasapi.patasunidasapi.repository.ImageStoreRepository;
import jakarta.transaction.Transactional;

@Service
public class AnimalProfileService {

    private final ImageStoreRepository imageStoreRepository;

    private final ImageConverter imageConverter;
    private final AnimalProfileRepository animalProfileRepository;

    public AnimalProfileService(AnimalProfileRepository animalProfileRepository, ImageConverter imageConverter, ImageStoreRepository imageStoreRepository) {
        this.animalProfileRepository = animalProfileRepository;
        this.imageConverter = imageConverter;
        this.imageStoreRepository = imageStoreRepository;
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
    public AnimalProfile convertToEntity(RegistrarAnimalProfileRequestDto dto) throws IOException{
        AnimalProfile animalProfile = new AnimalProfile();
        animalProfile.setCreatedByUserId(dto.getCreatedByUserId());
        animalProfile.setManagedByUserId(dto.getCreatedByUserId());
        animalProfile.setApproximateAge(dto.getApproximateAge());
        animalProfile.setCreatedAt(dto.getCreatedAt());
        animalProfile.setDescription(dto.getDescription());
        animalProfile.setLocation(new GeoLocation(dto.getLatitude(), dto.getLongitude()));
        animalProfile.setPhotos(photosDtoHandleRequest(dto.getPhotos()));
        animalProfile.setProvisionalName(dto.getProvisionalName());
        animalProfile.setSex(dto.getSex());
        animalProfile.setSize(dto.getSize());
        animalProfile.setStatus(dto.getStatus());

        return animalProfile;
    }

    //convert de response para busca
    public BuscarAnimalProfileResponseDto convertToDto(AnimalProfile animal){
        BuscarAnimalProfileResponseDto dto = new BuscarAnimalProfileResponseDto();
        dto.setId(animal.getId());
        dto.setCreatedByUserId(animal.getCreatedByUserId());
        dto.setManagedByUserId( animal.getManagedByUserId());
        dto.setPhotos(animal.getPhotos());
        dto.setLatitude( animal.getLocation().getLatitude());
        dto.setLongitude( animal.getLocation().getLongitude());
        dto.setStatus( animal.getStatus());
        dto.setCreatedAt( animal.getCreatedAt());
        dto.setProvisionalName( animal.getProvisionalName());
        dto.setDescription( animal.getDescription());
        dto.setSize( animal.getSize());
        dto.setSex( animal.getSex());
        dto.setApproximateAge(animal.getApproximateAge());
        
         return dto;
    }
    public BuscarAnimalProfileResponseDto convertToDto(AnimalProfile animal, double latitude, double longitude){
        BuscarAnimalProfileResponseDto dto = new BuscarAnimalProfileResponseDto();
        dto.setId(animal.getId());
        dto.setCreatedByUserId(animal.getCreatedByUserId());
        dto.setManagedByUserId( animal.getManagedByUserId());
        dto.setPhotos(animal.getPhotos());
        dto.setLatitude( animal.getLocation().getLatitude());
        dto.setLongitude( animal.getLocation().getLongitude());
        dto.setStatus( animal.getStatus());
        dto.setCreatedAt( animal.getCreatedAt());
        dto.setProvisionalName( animal.getProvisionalName());
        dto.setDescription( animal.getDescription());
        dto.setSize( animal.getSize());
        dto.setSex( animal.getSex());
        dto.setApproximateAge(animal.getApproximateAge());
        dto.setApproximateDistance(calcularDistancia(latitude, longitude, dto.getLatitude(), dto.getLongitude()));
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
    public AnimalProfile updateProfile(Long profileId, AtualizarAnimalProfileRequestDto animalDto) throws IOException{

        AnimalProfile animal = animalProfileRepository.findById(profileId).orElseThrow(() -> new NoSuchElementException("Perfil não encontrado com esse ID: " + profileId));
        if(animalDto.getProvisionalName() != null)
            animal.setProvisionalName(animalDto.getProvisionalName());
        if(animalDto.getDescription() != null)
            animal.setDescription(animalDto.getDescription());
        if(animalDto.getStatus() != null)
            animal.setStatus(animalDto.getStatus());
        if(animalDto.getPhotos() != null){
            animal.setPhotos(photosDtoHandleRequest(animalDto.getPhotos()));
        }
        if (animalDto.getLatitude() != null && animalDto.getLongitude() != null) 
            animal.setLocation(new GeoLocation(animalDto.getLatitude(), animalDto.getLongitude()));
        
        return animalProfileRepository.save(animal);
    }
    //converte todo o array e não resolve mais nada TO-DO apagar as que n estiverem mais salvas em um animal profile sei la nao vai pra frente mesmo esse projeto
    public ArrayList<String> photosDtoHandleRequest (ArrayList <String> photos) throws IOException {
        ArrayList<String> fotoshandler = new ArrayList<>();

        try {
            for (String foto : photos) {
                fotoshandler.add(imageConverter.decodeB64(foto));
            }

        } catch (IOException e) {
            throw new IOException("Erro ao tentar salvar foto", e);
        }
        return fotoshandler;
    }
    //passando a responsabilidade pro service
    public byte[] getImage(String fileName) throws IOException {
        try {
            return imageConverter.getImageBytes(fileName);
        } catch (Exception e) {
            throw new IOException();
        }
    }

    public List<BuscarAnimalProfileResponseDto> buscarAnimaisProximos(Double userLat, Double userLong) {
    // 1. Busca todos os animais (ou filtra por status antes de trazer, ex: apenas AVAILABLE)
    List<AnimalProfile> animais = animalProfileRepository.findAll(); 

    // 2. Converte para DTO e calcula a distância
    List<BuscarAnimalProfileResponseDto> dtos = animais.stream().map(animal -> {
        BuscarAnimalProfileResponseDto dto = convertToDto(animal); // Seu método converter existente
        
        if (userLat != null && userLong != null && animal.getLocation().getLatitude() != null && animal.getLocation().getLongitude() != null) {
            double distancia = calcularDistancia(userLat, userLong, animal.getLocation().getLatitude(), animal.getLocation().getLongitude());
            dto.setApproximateDistance(distancia);
        }
        return dto;
    }).collect(Collectors.toList());

    // 3. Ordena a lista (Menor distância primeiro)
    if (userLat != null && userLong != null) {
        dtos.sort(Comparator.comparingDouble(d -> {
            if (d.getApproximateDistance() == null) return Double.MAX_VALUE; // Joga pro fim se não tiver lat/long
            return d.getApproximateDistance();
        }));
    }

    return dtos;
    }
    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
    final int RAIO_TERRA = 6371000; // Raio da Terra em metros

    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return (RAIO_TERRA * c)/1000; // Resultado em km
}

    public AnimalProfile getAnimal (Long id) {
        Optional<AnimalProfile> animal = animalProfileRepository.findById(id);
       
        return animal.get();
    }

    public void deletarAnimal(Long id) {
    // 1. Busca o animal (precisamos dos dados dele antes de apagar)
    AnimalProfile animal = animalProfileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Animal não encontrado com id: " + id));

    // 2. Apaga as imagens associadas na tabela image_store
    if (animal.getPhotos() != null && !animal.getPhotos().isEmpty()) {
        for (String photoUuid : animal.getPhotos()) {
            // O UUID que está na lista 'photos' é a chave primária na tabela 'image_store'
            imageStoreRepository.deleteById(photoUuid);
        }
    }

    // 3. Finalmente, apaga o perfil do animal
    animalProfileRepository.delete(animal);
    }
}
