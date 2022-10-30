package eu.borglum.functional.core;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Result<T> {

    <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> function);

    <U> Result<U> map(Function<? super T, ? extends U> function);

    static <U> Result<U> of(OptionalSupplier<U> supplier) {
        Objects.requireNonNull(supplier);

        Optional<U> value;
        try {
            value = supplier.get();
        } catch (Exception e) {
            return Failure.create(e);
        }

        return Success.create(value);
    }

    static <U> Result<U> of(Supplier<U> supplier) {
        Objects.requireNonNull(supplier);

        U value;
        try {
            value = supplier.get();
        } catch (Exception e) {
            return Failure.create(e);
        }

        return Success.create(value);
    }
}
