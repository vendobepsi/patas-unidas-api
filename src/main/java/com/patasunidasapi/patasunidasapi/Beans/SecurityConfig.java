package com.patasunidasapi.patasunidasapi.Beans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
            // 1. DESABILITA CSRF (CORREÇÃO MAIS FORTE PARA POST/PUT/DELETE EM APIS REST)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. DESABILITA AUTENTICAÇÃO BASEADA EM SESSÃO (IMPORTANTE PARA JWT)
            // APIs REST sem estado devem ser STATELESS
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 3. Configura a Autorização
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/user/register-new-user").permitAll()
                .requestMatchers(HttpMethod.GET, "/user/check-email").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
            );

        // Remove filtros HTTP Basic Auth ou de formulário padrão
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);



        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
