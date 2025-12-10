package com.patasunidasapi.patasunidasapi.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.patasunidasapi.patasunidasapi.Utility.ImageConverter;
import com.patasunidasapi.patasunidasapi.dto.user.AtualizarPhotoDto;
import com.patasunidasapi.patasunidasapi.dto.user.LoginUsuarioResponseDto;
import com.patasunidasapi.patasunidasapi.dto.user.ReferenceUsuarioResponseDto;
import com.patasunidasapi.patasunidasapi.dto.user.RegistrarUsuarioRequestDto;
import com.patasunidasapi.patasunidasapi.model.User;
import com.patasunidasapi.patasunidasapi.model.UserType;
import com.patasunidasapi.patasunidasapi.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private UserRepository userRepository;
    private ImageConverter imageConverter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public UserService(UserRepository userRepository, ImageConverter imageConverter){
        this.userRepository = userRepository;
        this.imageConverter = imageConverter;
    }
    //convert de login
    public LoginUsuarioResponseDto ConvertToDto(User user, String token){
        LoginUsuarioResponseDto userDto = new LoginUsuarioResponseDto();

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setId(Long.toString(user.getId()));
        userDto.setToken(token);
        userDto.setUserType(user.getUserType());
        userDto.setCity(user.getCity());
        userDto.setState(user.getState());
        userDto.setVerifiedProtector(user.isVerifiedProtector());
        userDto.setProfilePictureUrl(user.getProfilePictureUrl());
        userDto.setHousingType(user.getHousingType());
        userDto.setHasOtherPets(user.isHasOtherPets());

        return userDto;
    }
    //convert de reference
    public ReferenceUsuarioResponseDto ConvertToDto(Long id){
        if (id == null) {
        throw new IllegalArgumentException("O ID informado é nulo.");
}
        User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        ReferenceUsuarioResponseDto userDto = new ReferenceUsuarioResponseDto(
        user.getName(),
        user.getProfilePictureUrl(),
        user.getId()
    );

        return userDto;
        }
        
    //convert de registro
    private User convertToEntity(RegistrarUsuarioRequestDto dto) throws IOException{
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha()); 
        if(dto.getUserPhotoUrl() != null){
            user.setProfilePictureUrl(imageConverter.decodeB64(dto.getUserPhotoUrl()));
        }
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setUserType(dto.getUserType());
        user.setHousingType(dto.getHousingType());
        user.setHasOtherPets(dto.isHasOtherPets());
        user.setVerifiedProtector(false);
        return user;
    }
    @Transactional
    public User registerNewUser(RegistrarUsuarioRequestDto dto)throws IOException{
        User usertoregister = convertToEntity(dto);

        String hashedpass = passwordEncoder.encode(usertoregister.getSenha());
        usertoregister.setSenha(hashedpass);

        return userRepository.save(usertoregister);
    }

    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    @Transactional
    public void alter(User user){
        userRepository.save(user);
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    public boolean isEmailAvailable(String email) {
        if (userRepository.existsByEmail(email)){
            return true;
        }
        return false;
        
    }
    public void updateUserPhoto(Long id, AtualizarPhotoDto dto) {
    // 1. Busca o usuário no banco (se não achar, estoura erro)
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    // 2. Altera APENAS o campo desejado na memória do Java
    user.setProfilePictureUrl(dto.getProfilePictureUrl());

    // 3. Salva de volta. O JPA detecta a mudança e faz o Update.
    userRepository.save(user);
}
public void deleteUserPhoto(Long userId) {
    // 1. Busca o usuário
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    // 2. Se tiver foto, apaga o arquivo físico
    String nomeArquivoAntigo = user.getProfilePictureUrl();
    
    if (nomeArquivoAntigo != null && !nomeArquivoAntigo.isEmpty()) {
        try {
            // Ajuste o caminho igual ao do seu ImageConverter
            Path caminhoArquivo = Paths.get("./src/main/resources/static/uploads", nomeArquivoAntigo + ".jpg");
            Files.deleteIfExists(caminhoArquivo);
        } catch (Exception e) {
            // Se der erro ao apagar o arquivo, apenas loga e segue a vida (não trava o banco)
            System.err.println("Erro ao apagar arquivo: " + e.getMessage());
        }
    }

    // 3. Limpa o campo no banco de dados
    user.setProfilePictureUrl(null); // ou "" se preferir
    userRepository.save(user);
}

}
