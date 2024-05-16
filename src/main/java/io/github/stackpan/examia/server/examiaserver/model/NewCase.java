package io.github.stackpan.examia.server.examiaserver.model;

import jakarta.validation.constraints.*;

public record NewCase(
        @NotNull @Size(max = 50) String title,
        @Size(max = 500) String description,
        @NotNull @Min(0) Integer durationInSeconds
) {

}
