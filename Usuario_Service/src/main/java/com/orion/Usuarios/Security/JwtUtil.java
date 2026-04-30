package com.orion.Usuarios.Security;


import com.orion.Usuarios.Entity.Rol;
import com.orion.Usuarios.Entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "0mfQfctbNBt7Tb4Ej8aXQQebUc8zmnhpkZKObqzxUCi";

    private static final long JWT_EXPIRATION = 86400000;


    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }


//    public String generateToken(UserDetails userDetails) {
//        return generateToken(new HashMap<>(), userDetails);
//    }

    public String generateToken(Usuario usuario){

        Map<String, Object> claims = new HashMap<>();

        claims.put("id", usuario.getId());

        claims.put("roles",usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                .compact();

    }



    public boolean isTokenValid(String token,UserDetails userDetails) {

        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }


    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());

    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
