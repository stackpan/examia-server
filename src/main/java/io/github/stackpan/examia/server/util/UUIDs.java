package io.github.stackpan.examia.server.util;

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
}
