package com.patasunidasapi.patasunidasapi.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.patasunidasapi.patasunidasapi.dto.user.LoginUsuarioRequestDto;
import com.patasunidasapi.patasunidasapi.dto.user.LoginUsuarioResponseDto;
import com.patasunidasapi.patasunidasapi.dto.user.RegistrarUsuarioRequestDto;
import com.patasunidasapi.patasunidasapi.repository.UserRepository;
import com.patasunidasapi.patasunidasapi.service.JwtService;
import com.patasunidasapi.patasunidasapi.service.UserDetailServiceImpl;
import com.patasunidasapi.patasunidasapi.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailServiceImpl userDetailServiceImpl;
    public UserController(UserRepository ur, UserService us, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailServiceImpl userDetailServiceImpl){
        this.userRepository = ur;
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
    public ResponseEntity<Boolean> postMethodName(@RequestBody RegistrarUsuarioRequestDto dto) {
        if(userService.isEmailAvailable(dto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 
        }

        userService.registerNewUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUsuarioResponseDto> authenticate(@RequestBody LoginUsuarioRequestDto request) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), 
                        request.getSenha() 
                )
        );
        
        
        String jwtToken = jwtService.generateToken(request.getEmail());

        // 4. Retorna o token para o frontend
        return ResponseEntity.ok(userService.ConvertToDto(userDetailServiceImpl.loadUserByUsername(request.getEmail()), jwtToken));
    }
    
    
}
