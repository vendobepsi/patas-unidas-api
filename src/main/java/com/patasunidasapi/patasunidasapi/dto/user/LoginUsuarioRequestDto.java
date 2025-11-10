package com.patasunidasapi.patasunidasapi.dto.user;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
public class LoginUsuarioRequestDto {
    private String email;
    private String senha;
}
