package com.patasunidasapi.patasunidasapi.dto.user;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
public class RegistrarUsuarioRequestDto {
    private String name;
    private String email;
    private String senha;

    private String city;
    private String state;
    private String userType;

    private String housingType;
    private boolean hasOtherPets;
}
