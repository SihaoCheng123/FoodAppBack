package com.app.foodapp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import
        io.jsonwebtoken.Claims
        ;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    //secretKey = jwt.secret del archivo application properties
    @Value("${jwt.secret}")
    private String secretKey;

    //fecha de caducidad
    //recomendadpo 30mins-1h. No más a menos que sea personal
    @Value("${jwt.expiration}")
    private Long expirationTime;

    //Para que nadie pueda saber la clave privada, la decodificamos
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
    }
    public String generateToken(String email){

        //JWT: Librería de seguridad
        //setSubject: Aquí añadimos los elementos qu eneviemos en el token
        //setIssuedAt: Fecha de creación del token
        //setExpiration: Cuándo caduca el token, se coge la fecha actual más la fecha de expiración
        //signWith: Firma con la clave privada
        //compact: Generar token público

        return Jwts.builder()
                .setSubject(email).
                setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.expirationTime))
                .signWith(this.getSigningKey()).
                compact();
    }

    public boolean validateToken(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build();
            parser.parseClaimsJws(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public String extractEmail(String token){
        JwtParser parser = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build();

        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
