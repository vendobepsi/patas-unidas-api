package com.patasunidasapi.patasunidasapi.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="app_user")
public class User implements UserDetails {
    @Id
	@SequenceGenerator(name="gerador2", sequenceName="app_user_id_seq", allocationSize=1)
    @GeneratedValue(generator = "gerador2", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String email;
    private String senha;

    private String phone;
    
    private String city;
    private String State;
    @Column(name = "user_type")
    private UserType userType;
    @Column(name = "is_verified_protector")
    private boolean isVerifiedProtector;
    @Column(name = "profile_picture_url")
    private String profilePictureUrl;
    @Column(name= "housing_type")
    private String housingType;

    @Column(name = "has_other_pets")
    private boolean hasOtherPets;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return State;
    }
    public void setState(String state) {
        State = state;
    }
    public UserType getUserType() {
        return userType;
    }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    public boolean isVerifiedProtector() {
        return isVerifiedProtector;
    }
    public void setVerifiedProtector(boolean isVerifiedProtector) {
        this.isVerifiedProtector = isVerifiedProtector;
    }
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    public String getHousingType() {
        return housingType;
    }
    public void setHousingType(String housingType) {
        this.housingType = housingType;
    }
    public boolean isHasOtherPets() {
        return hasOtherPets;
    }
    public void setHasOtherPets(boolean hasOtherPets) {
        this.hasOtherPets = hasOtherPets;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapeia o UserType para uma função de segurança (Role).
        // Por convenção, as funções Spring Security começam com "ROLE_".
        
        String roleName = "ROLE" + this.userType.name(); // Ex: ROLE_PROTECTOR_ONG
        
        // Retorna uma lista imutável contendo apenas a função do usuário
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }
    @Override
    public String getPassword() {
        return senha;
    }
    @Override
    public String getUsername() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
