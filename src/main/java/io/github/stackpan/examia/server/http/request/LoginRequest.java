package io.github.stackpan.examia.server.http.request;

import io.github.stackpan.examia.server.validator.StringOnly;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotNull @StringOnly @Size(max = 50) String username,
        @NotNull @StringOnly @Size(max = 32) String password
) {
}
