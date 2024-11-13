package br.com.liven.shopping.cart.service;

import br.com.liven.shopping.cart.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("secret.key")
    private String secretKey;

    @SneakyThrows
    public String tokenGenerate(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        try {
            return JWT.create().withIssuer("shoppingCart").withSubject(user.getPerson().getEmail())
                    .withExpiresAt(createDateOfExpirition()).sign(algorithm);
        } catch (JWTCreationException e) {
            throw new BadCredentialsException("Error on generate token");
        }
    }

    private Instant createDateOfExpirition() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String verifyToken(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("shoppingCart")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e) {
            return "";
        }
    }

}