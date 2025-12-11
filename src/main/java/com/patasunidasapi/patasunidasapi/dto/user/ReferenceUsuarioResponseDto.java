package com.patasunidasapi.patasunidasapi.dto.user;
import lombok.Getter;
import lombok.Setter;

import com.patasunidasapi.patasunidasapi.model.UserType;

import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
public class ReferenceUsuarioResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String city;
    private String state;
    private UserType userType;
    private boolean isVerifiedProtector;
    private String profilePictureUrl;

    private String housingType;
    private boolean hasOtherPets;
}
