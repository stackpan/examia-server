package io.github.stackpan.examia.server.http.resource;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.List;

public record JwtResource(String token, List<String> scopes) {

    public static JwtResource fromJwt(Jwt jwt) {
        var scope = Arrays.stream(jwt.getClaimAsString("scope").split(" ")).toList();

        return new JwtResource(jwt.getTokenValue(), scope);
    }
}
