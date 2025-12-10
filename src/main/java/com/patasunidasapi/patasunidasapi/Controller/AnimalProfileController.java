package com.patasunidasapi.patasunidasapi.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patasunidasapi.patasunidasapi.Utility.ImageConverter;
import com.patasunidasapi.patasunidasapi.dto.animalprofile.AtualizarAnimalProfileRequestDto;
import com.patasunidasapi.patasunidasapi.dto.animalprofile.BuscarAnimalProfileResponseDto;
import com.patasunidasapi.patasunidasapi.dto.animalprofile.RegistrarAnimalProfileRequestDto;
import com.patasunidasapi.patasunidasapi.model.AnimalProfile;
import com.patasunidasapi.patasunidasapi.service.AnimalProfileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/animalprofile")
public class AnimalProfileController {

    private AnimalProfileService animalProfileService;

    public AnimalProfileController(AnimalProfileService animalProfileService) {

        this.animalProfileService = animalProfileService;
    }
    
    //to - do
    @GetMapping("/get-all")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/register-animal-profile")
    public ResponseEntity<Boolean> registerAnimalProfile(@RequestBody RegistrarAnimalProfileRequestDto dto){
        try {
            animalProfileService.registerAnimalProfile(animalProfileService.convertToEntity(dto));

            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            System.err.println("Erro ao salvar perfil animal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PatchMapping("/alter-{id}")
    public ResponseEntity<AnimalProfile> updateAnimalProfile(@PathVariable Long id, @RequestBody AtualizarAnimalProfileRequestDto dto){
        try{
            AnimalProfile updatedProfile = animalProfileService.updateProfile(id, dto);

            return ResponseEntity.ok(updatedProfile);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get-recent")
    public ResponseEntity<List<BuscarAnimalProfileResponseDto>> getAllRecent() {
        List<AnimalProfile> profiles = animalProfileService.findAllNewestFirst();
        List<BuscarAnimalProfileResponseDto> profilesdto = new ArrayList<BuscarAnimalProfileResponseDto>();
        for (AnimalProfile animal : profiles){
            profilesdto.add(animalProfileService.convertToDto(animal));
        }

        return ResponseEntity.ok(profilesdto);
    }

    @GetMapping("/get-oldest")
    public ResponseEntity<List<BuscarAnimalProfileResponseDto>> getAllOldest() {
        List<AnimalProfile> profiles = animalProfileService.findAllOldestFirst();
        List<BuscarAnimalProfileResponseDto> profilesdto = new ArrayList<BuscarAnimalProfileResponseDto>();
        for (AnimalProfile animal : profiles){
            profilesdto.add(animalProfileService.convertToDto(animal));
        }

        return ResponseEntity.ok(profilesdto);
    }

    @GetMapping("/image/{fileName}") 
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            byte[] imageBytes = animalProfileService.getImage(fileName);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
                    
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/proximos")
    public ResponseEntity<List<BuscarAnimalProfileResponseDto>> listarPorProximidade(
        @RequestParam(required = false) Double latitude,
        @RequestParam(required = false) Double longitude) {
    
    return ResponseEntity.ok(animalProfileService.buscarAnimaisProximos(latitude, longitude));
    }
}
