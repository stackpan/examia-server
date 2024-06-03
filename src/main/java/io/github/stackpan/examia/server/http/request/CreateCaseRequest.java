package io.github.stackpan.examia.server.http.request;

import jakarta.validation.constraints.*;

public record CreateCaseRequest(
        @NotNull @Size(max = 50) String title,
        @Size(max = 500) String description,
        @NotNull @Min(0) Integer durationInSeconds
) {

}
