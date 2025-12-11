package com.patasunidasapi.patasunidasapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReferenceUsuarioResponseDto {
    private String name;
    private String profilePictureUrl;
    private String phone;
    private Long id;
}
