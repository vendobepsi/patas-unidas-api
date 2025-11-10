package com.patasunidasapi.patasunidasapi.dto.user;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUsuarioResponseDto {
    private String token;
    private String id;
    private String name;
    private String email;

    private String city;
    private String state;
    private String userType;
    private boolean isVerifiedProtector;
    private String profilePictureUrl;

    private String housingType;
    private boolean hasOtherPets;
}
