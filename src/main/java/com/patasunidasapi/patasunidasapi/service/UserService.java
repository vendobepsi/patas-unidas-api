package com.patasunidasapi.patasunidasapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.patasunidasapi.patasunidasapi.dto.user.LoginUsuarioResponseDto;
import com.patasunidasapi.patasunidasapi.dto.user.RegistrarUsuarioRequestDto;
import com.patasunidasapi.patasunidasapi.model.User;
import com.patasunidasapi.patasunidasapi.model.UserType;
import com.patasunidasapi.patasunidasapi.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public LoginUsuarioResponseDto ConvertToDto(User user, String token){
        LoginUsuarioResponseDto userDto = new LoginUsuarioResponseDto();

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setId(Long.toString(user.getId()));
        userDto.setToken(token);
        switch(user.getUserType()){
            case UserType.COMMON:
            userDto.setUserType("common");
            break;
            case UserType.PROTECTOR_ONG:
            userDto.setUserType("protector_ong");
            break;
            case UserType.ADOPTER:
            userDto.setUserType("adopter");
            break;
        }
        userDto.setVerifiedProtector(user.isVerifiedProtector());
        userDto.setProfilePictureUrl(user.getProfilePictureUrl());
        userDto.setHousingType(user.getHousingType());
        userDto.setHasOtherPets(user.isHasOtherPets());

        return userDto;
    }
    private User convertToEntity(RegistrarUsuarioRequestDto dto){
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha()); 
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        switch(dto.getUserType()){
            case "commom":
            user.setUserType(UserType.COMMON);
            break;
            case "protector_ong":
            user.setUserType(UserType.PROTECTOR_ONG);
            break;
            case "adopter":
            user.setUserType(UserType.ADOPTER);
            break;
        }
        user.setHousingType(dto.getHousingType());
        user.setHasOtherPets(dto.isHasOtherPets());
        user.setVerifiedProtector(false);
        return user;
    }
    @Transactional
    public User registerNewUser(RegistrarUsuarioRequestDto dto){
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
