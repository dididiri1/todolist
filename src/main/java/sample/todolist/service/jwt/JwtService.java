package sample.todolist.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;
import sample.todolist.config.jwt.JwtProperties;
import sample.todolist.dto.jwt.JwtResponse;

import java.util.Date;

@Service
public class JwtService {
    public JwtResponse createToken(String username) {
        String token = JWT.create()
                .withSubject(JwtProperties.SECRET)
                .withExpiresAt(new Date(System.currentTimeMillis()+(JwtProperties.EXPIRATION_TIME)))
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));

        return JwtResponse.builder()
                .accessToken(token)
                .build();
    }
}
