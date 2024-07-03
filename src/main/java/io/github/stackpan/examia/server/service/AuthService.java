package io.github.stackpan.examia.server.service;

import io.github.stackpan.examia.server.http.request.LoginRequest;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthService {

    Jwt login(LoginRequest credentials);

}
