package io.github.stackpan.examia.server.service.impl;

import io.github.stackpan.examia.server.http.request.LoginRequest;
import io.github.stackpan.examia.server.security.Tokenizer;
import io.github.stackpan.examia.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final Tokenizer tokenizer;

    @Override
    public Jwt login(LoginRequest credentials) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
        var authenticated = authenticationManager.authenticate(authenticationToken);

        return tokenizer.generate(authenticated);
    }
}
