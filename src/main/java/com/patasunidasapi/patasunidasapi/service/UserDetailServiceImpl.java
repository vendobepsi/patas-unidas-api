package com.patasunidasapi.patasunidasapi.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.patasunidasapi.patasunidasapi.model.User;
import com.patasunidasapi.patasunidasapi.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
    
    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository ur){
        this.userRepository = ur;
    }
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<User> oUser = userRepository.findByEmail(email);
        
        if (!oUser.isPresent()){
           throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
        }
        return oUser.get();
    }
}
