package eu.borglum.functional.core;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Result<T> {

    /**
     * @param predicate
     * @return
     * @since 1.0
     */
    Result<T> filter(Predicate<? super T> predicate);

    /**
     * @param function
     * @param <U>
     * @return
     * @since 1.0
     */
    <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> function);

    /**
     * @param function
     * @param <U>
     * @return
     * @since 1.0
     */
    <U> Result<U> map(Function<? super T, ? extends U> function);

    /**
     * @param function
     * @param <U>
     * @return
     * @sinc 1.0
     */
    <U> Result<U> map(OptionalFunction<? super T, ? extends U> function);

    /**
     * @param supplier
     * @param <U>
     * @return
     * @since 1.0
     */
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

    /**
     * @param supplier
     * @param <U>
     * @return
     * @since 1.0
     */
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
