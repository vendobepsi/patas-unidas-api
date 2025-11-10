package com.patasunidasapi.patasunidasapi.service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private Long jtwExpiration;

    public String generateToken(String email) {
        return buildToken(new HashMap<>(), email, jtwExpiration);
        
    }

    private String buildToken(Map<String, Object> extraClaims, String email, Long expiration) {
       return Jwts.builder().setClaims(extraClaims).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+expiration)).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignInKey() {
        byte[] keybites = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keybites);
    }
}
