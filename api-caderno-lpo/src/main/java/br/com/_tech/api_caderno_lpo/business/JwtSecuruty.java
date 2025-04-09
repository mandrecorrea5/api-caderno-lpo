package br.com._tech.api_caderno_lpo.business;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtSecuruty {
  private static final String ISSUER = "caderno-lpo";

  @Value("${jwt.secret}")
  private String secret;

  private Algorithm algorithm;

  @PostConstruct
  public void init() {
    this.algorithm = Algorithm.HMAC256(secret);
  }

  public String gerarToken(UUID usuarioId, String email){
    return JWT.create()
        .withIssuer(ISSUER)
        .withSubject(email)
        .withClaim("id", usuarioId.toString())
        .withIssuedAt(new Date())
        .withExpiresAt(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)))
        .sign(algorithm);
  }

  public DecodedJWT validarToken(String token) throws JWTVerificationException {
    JWTVerifier verifier = JWT.require(algorithm)
        .withIssuer(ISSUER)
        .build();

    return verifier.verify(token);
  }

  public String obterEmailUsuario(String token) {
    return validarToken(token).getSubject();
  }

  public UUID obterIdUsuario(String token) {
    return UUID.fromString(validarToken(token).getClaim("id").asString());
  }
}
