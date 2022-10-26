package eu.borglum.functional.core;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public interface Result<T> {

    static <U> Result<U> of(OptionalSupplier<U> supplier) {
        try {
            return new Success<>(Objects.requireNonNull(supplier.get()));
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    static <U> Result<U> of(Supplier<U> supplier) {
        try {
            return new Success<>(Optional.ofNullable(supplier.get()));
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }
}
