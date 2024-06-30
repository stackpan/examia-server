package io.github.stackpan.examia.server.http.controller;

import io.github.stackpan.examia.server.http.resource.UserResource;
import io.github.stackpan.examia.server.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/me")
    public UserResource me(Authentication authentication) {
        return UserResource.fromEntity((User) authentication.getPrincipal());
    }

}
