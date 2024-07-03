package io.github.stackpan.examia.server.http.controller;

import io.github.stackpan.examia.server.http.request.LoginRequest;
import io.github.stackpan.examia.server.http.resource.JwtResource;
import io.github.stackpan.examia.server.http.resource.UserResource;
import io.github.stackpan.examia.server.service.AuthService;
import io.github.stackpan.examia.server.service.ExamiaUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final ExamiaUserService examiaUserService;

    @GetMapping("/me")
    public UserResource me(JwtAuthenticationToken authentication) {
        var authenticatedUserId = (String) authentication.getTokenAttributes().get("sub");
        var user = examiaUserService.getById(authenticatedUserId);

        return UserResource.fromEntity(user);
    }

    @PostMapping("/login")
    public JwtResource login(@RequestBody @Valid LoginRequest request) {
        var jwt = authService.login(request);

        return JwtResource.fromJwt(jwt);
    }

}
