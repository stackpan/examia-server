package io.github.stackpan.examia.server.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;

    private String resourceId;

    @Override
    public String getMessage() {
        return "Cannot find %s with identity: %s".formatted(resourceName, resourceId);
    }
}
