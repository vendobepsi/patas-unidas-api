package com.patasunidasapi.patasunidasapi.service;


import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.patasunidasapi.patasunidasapi.Utility.ImageConverter;
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
}
