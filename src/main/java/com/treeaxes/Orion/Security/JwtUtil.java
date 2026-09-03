package com.treeaxes.Orion.Security;


import com.treeaxes.Orion.Model.Rol;
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
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // Clave secreta para firmar el token
    @Value("${jwt.secret}")
    private String secret;

    // Tiempo de expiración del token en milisegundos
    @Value("${jwt.expiration_time}")
    private Long expirationTime;

    /// Genera un token JWT para un usuario dado
    /// @param usuario El usuario para el cual se generará el token
    /// @return El token JWT generado
    public String generateToken(Usuario usuario) {
        //Date now = new Date(System.currentTimeMillis());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("roles", usuario.getRoles()
                .stream().map(Rol::getName).collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    /// Obtiene la clave de firma para el token
    /// @return La clave de firma
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
