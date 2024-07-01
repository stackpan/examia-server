package io.github.stackpan.examia.server.http.controller;

import io.github.stackpan.examia.server.http.request.LoginRequest;
import io.github.stackpan.examia.server.http.resource.JwtResource;
import io.github.stackpan.examia.server.http.resource.UserResource;
import io.github.stackpan.examia.server.entity.User;
import io.github.stackpan.examia.server.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public UserResource me(Authentication authentication) {
        return UserResource.fromEntity((User) authentication.getPrincipal());
    }

    @PostMapping("/login")
    public JwtResource login(@RequestBody @Valid LoginRequest request) {
        var jwt = authService.login(request);

        return JwtResource.fromJwt(jwt);
    }

}
