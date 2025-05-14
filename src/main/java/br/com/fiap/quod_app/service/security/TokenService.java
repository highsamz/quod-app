package br.com.fiap.quod_app.service.security;

import br.com.fiap.quod_app.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final  String SECRET = "ULTIMATO";

    public String createToken (User user){

        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        Instant expirexAt = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
        String token = JWT.create()
                .withIssuer("umbl")
                .withSubject(user.getEmail())
                .withExpiresAt(expirexAt)
                .sign(algorithm);
        return token;
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.require(algorithm)
                    .withIssuer("umbl")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            return "";
        }
    }
}
