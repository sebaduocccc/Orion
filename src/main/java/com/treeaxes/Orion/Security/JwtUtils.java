package com.treeaxes.Orion.Security;


import com.treeaxes.Orion.Model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration_time}")
    private Long expirationTime;

    public String generateToken(Usuario usuario) {
        Date now = new Date(System.currentTimeMillis());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
