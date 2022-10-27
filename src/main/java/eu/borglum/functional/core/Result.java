package eu.borglum.functional.core;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Result<T> {

    <U> Result<U> map(Function<? super T, ? extends U> function);

    static <U> Result<U> of(OptionalSupplier<U> supplier) {
        Objects.requireNonNull(supplier);

        Optional<U> value;
        try {
            value = supplier.get();
        } catch (Exception e) {
            return new Failure<>(e);
        }

        return new Success<>(Objects.requireNonNull(value));
    }

    static <U> Result<U> of(Supplier<U> supplier) {
        Objects.requireNonNull(supplier);

        try {
            return new Success<>(Optional.ofNullable(supplier.get()));
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }
}
