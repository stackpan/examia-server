package io.github.stackpan.examia.server.http.request;

import io.github.stackpan.examia.server.validator.StringOnly;
import jakarta.validation.constraints.*;

public record CreateCaseRequest(
        @NotNull @StringOnly @Size(max = 50) String title,
        @StringOnly @Size(max = 500) String description,
        @NotNull @Min(0) Integer durationInSeconds
) {

}
