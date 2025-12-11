package com.patasunidasapi.patasunidasapi.Controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.patasunidasapi.patasunidasapi.dto.user.AtualizarPhotoDto;
import com.patasunidasapi.patasunidasapi.dto.user.LoginUsuarioRequestDto;
import com.patasunidasapi.patasunidasapi.dto.user.LoginUsuarioResponseDto;
import com.patasunidasapi.patasunidasapi.dto.user.ReferenceUsuarioResponseDto;
import com.patasunidasapi.patasunidasapi.dto.user.RegistrarUsuarioRequestDto;
import com.patasunidasapi.patasunidasapi.service.JwtService;
import com.patasunidasapi.patasunidasapi.service.UserDetailServiceImpl;
import com.patasunidasapi.patasunidasapi.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailServiceImpl userDetailServiceImpl;
    public UserController(UserService us, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailServiceImpl userDetailServiceImpl){
        this.userService = us;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailServiceImpl = userDetailServiceImpl;
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {

        boolean check = userService.isEmailAvailable(email);
        return ResponseEntity.ok(check);
    }

    @PostMapping("/register-new-user")
    public ResponseEntity<?> registerNewUser(@RequestBody RegistrarUsuarioRequestDto dto) {
        if(userService.isEmailAvailable(dto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 
        }
        try {
            userService.registerNewUser(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao processar imagem: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUsuarioResponseDto> authenticate(@RequestBody LoginUsuarioRequestDto request) {
        System.out.println(">>> CHEGOU NO CONTROLLER! Email recebido: " + request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), 
                        request.getSenha() 
                )
        );
        
        
        String jwtToken = jwtService.generateToken(request.getEmail());

        //retorna o user e o token
        return ResponseEntity.ok(userService.ConvertToDto(userDetailServiceImpl.loadUserByUsername(request.getEmail()), jwtToken));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReferenceUsuarioResponseDto> getReference(@PathVariable Long id) {

        return ResponseEntity.ok(userService.ConvertToDto(id));
    }

    @PatchMapping("/{id}/update-photo")
    public ResponseEntity<Void> updateUserPhoto(@PathVariable Long id, @RequestBody AtualizarPhotoDto dto) throws IOException{
        userService.updateUserPhoto(id, dto);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}/delete-photo")
    public ResponseEntity<Void> updateUserPhoto(@PathVariable Long id){
        userService.deleteUserPhoto(id);
        return ResponseEntity.noContent().build();
    }
    
    
}
