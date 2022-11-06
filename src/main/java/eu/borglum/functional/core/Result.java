package eu.borglum.functional.core;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Result<T> {

    /**
     * If the {@link Result} is currently a {@code success} apply the {@link Predicate} and return
     * the outcome as a new {@link Result}.
     * <p>
     * If the {@link Result} is currently a {@code failure} do not apply the {@link Predicate} and return
     * a new {@link Result} containing the {@code failure}.
     *
     * @param predicate the {@link Predicate} to apply
     * @return a new {@link Result} after the {@link Predicate} has been applied
     * @throws NullPointerException if the {@link Predicate} is {@code null}
     * @since 1.0
     */
    Result<T> filter(Predicate<? super T> predicate);

    /**
     * If the {@link Result} is currently a {@code success} apply the {@link Function} and return
     * the outcome as a new {@link Result}.
     * <p>
     * If the {@link Result} currently is a {@code failure} do not apply the {@link Function} and return
     * a new {@link Result} containing the {@code failure}.
     * <p>
     * This method is similar to {@link #map(Function)}, but the mapping function is one whose result is already
     * a {@link Result}, and if invoked, {@code flatMap} does not wrap it within an additional {@link Result}.
     *
     * @param function the {@link Function} to apply
     * @param <U>      the type of the value of the {@link Result} returned by the {@link Function}
     * @return a new {@link Result} after the {@link Function} has been applied
     * @throws NullPointerException if the {@link Function} is {@code null}
     * @since 1.0
     */
    <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> function);

    /**
     * If the {@link Result} is currently a {@code success} apply the {@link Function} and return
     * the outcome as a new {@link Result}.
     * <p>
     * If the {@link Result} currently is a {@code failure} do not apply the {@link Function} and return
     * a new {@link Result} containing the {@code failure}.
     *
     * @param function the {@link Function} to apply
     * @param <U>      the type of the value returned by the {@link Function}
     * @return a new {@link Result} after the {@link Function} has been applied
     * @throws NullPointerException if the {@link Function} is {@code null}
     * @since 1.0
     */
    <U> Result<U> map(Function<? super T, ? extends U> function);

    /**
     * If the {@link Result} is currently a {@code success} apply the {@link OptionalFunction} and return
     * the outcome as a new {@link Result}.
     * <p>
     * If the {@link Result} currently is a {@code failure} do not apply the {@link OptionalFunction} and return
     * a new {@link Result} containing the {@code failure}.
     * <p>
     * This method is similar to {@link #map(Function)}, but the mapping function is one whose result is already
     * an {@link Optional}, and if invoked, {@code map} does not wrap it within an additional {@link Optional}.
     *
     * @param function the {@link OptionalFunction} to apply
     * @param <U>      the type of the value of the {@link Optional} returned by the {@link OptionalFunction}
     * @return a new {@link Result} after the {@link OptionalFunction} has been applied
     * @throws NullPointerException if the {@link OptionalFunction} is {@code null}
     * @since 1.0
     */
    <U> Result<U> map(OptionalFunction<? super T, ? extends U> function);

    /**
     * Creates a {@link Result} by applying the {@link OptionalSupplier}
     *
     * @param supplier the {@link OptionalSupplier} to apply
     * @param <U>      the type of the value of the {@link Optional} returned by the {@link OptionalSupplier}
     * @return the {@link Result} of applying the {@link OptionalSupplier}
     * @throws NullPointerException if the {@link OptionalSupplier} is {@code null}
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
     * Creates a {@link Result} by applying the {@link Supplier}
     *
     * @param supplier the {@link Supplier} to apply
     * @param <U>      the type of the value returned by the {@link Supplier}
     * @return the {@link Result} of applying the {@link Supplier}
     * @throws NullPointerException if the {@link Supplier} is {@code null}
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
