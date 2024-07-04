package io.github.stackpan.examia.server.util;

import io.github.stackpan.examia.server.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.util.function.Supplier;

@AllArgsConstructor
public class UUIDs {

    private String value;

    public static UUIDs fromString(String value) {
        return new UUIDs(value);
    }

    public <X extends Throwable> UUID orElseThrows(Supplier<? extends X> exceptionSupplier) throws X {
        try {
            return java.util.UUID.fromString(value);
        } catch (IllegalArgumentException ignored) {
            throw exceptionSupplier.get();
        }
    }

    public UUID orElseThrowsResourceNotFound(String resourceName) {
        return orElseThrows(() -> new ResourceNotFoundException(resourceName, value));
    }

    public UUID orElseThrowsResourceNotFound(Class<?> resourceClass) {
        return orElseThrows(() -> new ResourceNotFoundException(resourceClass.getSimpleName(), value));
    }
}
