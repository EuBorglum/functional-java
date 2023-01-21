package eu.borglum.functional.core;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Result<T> {

    /**
     * If the {@link Result} is currently a {@code success} apply the {@link Predicate} and return
     * a new {@link Result} as either a {@code success} or a {@code failure} depending on the outcome of the
     * {@link Predicate}.
     * <p>
     * If the {@link Result} is currently a {@code failure} do not apply the {@link Predicate} and return
     * a new {@link Result} as a {@code failure} containing the {@link Exception} of the current {@code failure}.
     *
     * @param predicate the {@link Predicate} to apply.
     * @return a new {@link Result} after the {@link Predicate} has been applied.
     * @throws NullPointerException if the {@link Predicate} is {@code null}.
     * @since 1.0
     */
    Result<T> filter(Predicate<? super T> predicate);

    /**
     * If the {@link Result} is currently a {@code success} apply the {@link Function} and return a new {@link Result}
     * as either a {@code success} or a {@code failure} depending on the outcome of the {@link Function}.
     * <p>
     * If the {@link Result} currently is a {@code failure} do not apply the {@link Function} and return
     * a new {@link Result} as a {@code failure} containing the {@link Exception} of the current {@code failure}.
     * <p>
     * This method is similar to {@link #map(Function)}, but the mapping function is one whose result is already
     * a {@link Result}, and if invoked, {@code flatMap} does not wrap it within an additional {@link Result}.
     *
     * @param function the {@link Function} to apply
     * @param <U>      the type of the value of the {@link Result} returned by the {@link Function}
     * @return a new {@link Result} after the {@link Function} has been applied
     * @throws NullPointerException if the {@link Function} is {@code null} or if the {@link Result} is
     *                              currently a {@code success} and the {@link Function} returns {@code null}
     * @since 1.0
     */
    <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> function);

    /**
     * If the {@link Result} is currently a {@code success} apply the {@link Function} and return a new {@link Result}
     * as either a {@code success} or a {@code failure} depending on the outcome of the {@link Function}.
     * <p>
     * If the {@link Result} currently is a {@code failure} do not apply the {@link Function} and return
     * a new {@link Result} as a {@code failure} containing the {@link Exception} of the current {@code failure}.
     *
     * @param function the {@link Function} to apply.
     * @param <U>      the type of the value returned by the {@link Function}.
     * @return a new {@link Result} to which the {@link Function} might have been applied.
     * @throws NullPointerException if the {@link Function} is {@code null} or if the {@link Result} is
     *                              currently a {@code success} and the {@link Function} returns {@code null}.
     * @since 1.0
     */
    <U> Result<U> map(Function<? super T, ? extends U> function);

    /**
     * If the {@link Result} is currently a {@code success} apply the {@link OptionalFunction} and return
     * a new {@link Result} as either a {@code success} or a {@code failure} depending on the outcome of the
     * {@link Function}.
     * <p>
     * If the {@link Result} currently is a {@code failure} do not apply the {@link OptionalFunction} and return
     * a new {@link Result} as a {@code failure} containing the {@link Exception} of the current {@code failure}.
     * <p>
     * This method is similar to {@link #map(Function)}, but the mapping function is one whose result is already
     * an {@link Optional}, and if invoked, {@code map} does not wrap it within an additional {@link Optional}.
     *
     * @param function the {@link OptionalFunction} to apply.
     * @param <U>      the type of the value of the {@link Optional} returned by the {@link OptionalFunction}.
     * @return a new {@link Result} after the {@link OptionalFunction} has been applied.
     * @throws NullPointerException if the {@link OptionalFunction} is {@code null} or if the {@link Result} is
     *                              currently a {@code success} and the {@link OptionalFunction} returns {@code null}.
     * @since 1.0
     */
    <U> Result<U> map(OptionalFunction<? super T, ? extends U> function);

    /**
     * @param supplier
     * @param <U>
     * @return
     * @since 1.0
     */
    <U> Result<U> map(SwitchSupplier<? super T, ? extends U> supplier);

    /**
     * If the {@link Result} is currently a {@code success} do not apply the {@link Function} and return a new
     * {@link Result} as a {@code success} containing the value of current {@code success}.
     * <p>
     * If the {@link Result} is currently a {@code failure} and the {@link Exception} in the {@code failure} cannot
     * be cast to the {@code exceptionClass} do not apply the {@link Function} and return a new {@link Result}
     * as a {@code failure} containing the {@link Exception} of the current {@code failure}.
     * <p>
     * If the {@link Result} is currently a {@code failure} and the {@link Exception} in the {@code failure} can be
     * cast to the {@code exceptionClass} apply the {@link Function} and return a new {@link Result} as a
     * {@code failure} containing the outcome of the {@link Function}.
     *
     * @param exceptionClass the {@link Class} predicate used to determine if the {@link Function} should be applied
     *                       to the {@code failure} or not.
     * @param function       the {@link Function} to apply.
     * @param <X>            the type of {@link Exception} that is mapped by the {@link Function}.
     * @return a new {@link Result} to which the {@link Function} might have been applied.
     * @throws NullPointerException if the {@code exceptionClass} is {@code null},
     *                              if the {@link Function} is {@code null} or
     *                              if the {@link Result} is currently a {@code failure} and the {@link Function}
     *                              is applied and it returns {@code null}.
     * @since 1.0
     */
    <X extends Exception> Result<T> mapFailure(Class<X> exceptionClass,
                                               Function<? super X, ? extends Exception> function);

    /**
     * A convenience method that does the same as {@link #map(OptionalFunction)}. It might be used to avoid casting
     * the {@code function} to an {@link OptionalFunction} if defined inline as a lambda expression.
     *
     * @param function the {@link OptionalFunction} to apply.
     * @param <U>      the type of the value of the {@link Optional} returned by the {@link OptionalFunction}
     * @return a new {@link Result} after the {@link OptionalFunction} has been applied.
     * @throws NullPointerException if the {@link OptionalFunction} is {@code null} or if the {@link Result} is
     *                              currently a {@code success} and the {@link OptionalFunction} returns {@code null}.
     * @since 1.0
     */
    <U> Result<U> mapOptional(OptionalFunction<? super T, ? extends U> function);

    /**
     * A convenience method that does the same as {@link #map(Function)}. It might be used to avoid casting the
     * {@code function} to a {@link Function} if defined inline as a lambda expression.
     *
     * @param function the {@link Function} to apply
     * @param <U>      the type of the value returned by the {@link Function}
     * @return a new {@link Result} after the {@link Function} has been applied
     * @throws NullPointerException if the {@link Function} is {@code null} or if the {@link Result} is
     *                              currently a {@code success} and the {@link Function} returns {@code null}
     * @since 1.0
     */
    <U> Result<U> mapValue(Function<? super T, ? extends U> function);

    /**
     * Apply the {@link OptionalSupplier} and create a {@link Result} as either a {@code success} or a {@code failure}
     * depending on the outcome of the {@link OptionalSupplier}.
     *
     * @param supplier the {@link OptionalSupplier} to apply.
     * @param <U>      the type of the value of the {@link Optional} returned by the {@link OptionalSupplier}.
     * @return the {@link Result} of applying the {@link OptionalSupplier}.
     * @throws NullPointerException if the {@link OptionalSupplier} is {@code null} or if the {@link OptionalSupplier}
     *                              returns {@code null}.
     * @since 1.0
     */
    static <U> Result<U> of(OptionalSupplier<U> supplier) {
        return ofOptional(supplier);
    }

    /**
     * Apply the {@link Supplier} and create a {@link Result} as either a {@code success} or {@code failure} depending
     * on the outcome of the {@link Supplier}.
     *
     * @param supplier the {@link Supplier} to apply.
     * @param <U>      the type of the value returned by the {@link Supplier}.
     * @return the {@link Result} of applying the {@link Supplier}.
     * @throws NullPointerException if the {@link Supplier} is {@code null} or if the {@link Supplier} returns
     *                              {@code null}.
     * @since 1.0
     */
    static <U> Result<U> of(Supplier<U> supplier) {
        return ofValue(supplier);
    }

    /**
     * A convenience method that does the same as {@link #of(OptionalSupplier)}. It might be used to avoid casting
     * the {@code supplier} to a {@link OptionalSupplier} if defined inline as a lambda expression.
     *
     * @param supplier the {@link OptionalSupplier} to apply.
     * @param <U>      the type of the value of the {@link Optional} returned by the {@link OptionalSupplier}.
     * @return the {@link Result} of applying the {@link OptionalSupplier}.
     * @throws NullPointerException if the {@link OptionalSupplier} is {@code null} or if the {@link OptionalSupplier}
     *                              returns {@code null}.
     * @since 1.0
     */
    static <U> Result<U> ofOptional(OptionalSupplier<U> supplier) {
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
     * A convenience method that does the same as {@link #of(Supplier)}. It might be used to avoid casting
     * the {@code supplier} to a {@link Supplier} if defined as a lambda expression.
     *
     * @param supplier the {@link Supplier} to apply.
     * @param <U>      the type of the value returned by the {@link Supplier}.
     * @return the {@link Result} of applying the {@link Supplier}.
     * @throws NullPointerException if the {@link Supplier} is {@code null} or if the {@link Supplier} returns
     *                              {@code null}.
     * @since 1.0
     */
    static <U> Result<U> ofValue(Supplier<U> supplier) {
        Objects.requireNonNull(supplier);

        U value;
        try {
            value = supplier.get();
        } catch (Exception e) {
            return Failure.create(e);
        }

        return Success.create(value);
    }

    /**
     * @param supplier
     * @return
     * @since 1.0
     */
    T orElseRecover(SwitchSupplier<Exception, T> supplier);

    /**
     * @return
     * @since 1.0
     */
    T orElseThrow();

    /**
     * If the {@link Result} is currently a {@code success} do not apply the {@link Function} and return a new
     * {@link Result} as a {@code success} containing the value of the current {@code success}.
     * <p>
     * If the {@link Result} is currently a {@code failure} and the {@link Exception} in the {@code failure} cannot
     * be cast to the {@code exceptionClass} do not apply the {@link Function} and return a new {@link Result}
     * as a {@code failure} containing the {@link Exception} of the current {@code failure}.
     * <p>
     * If the {@link Result} is currently a {@code failure} and the {@link Exception} in the {@code failure} can be
     * cast to the {@code exceptionClass} apply the {@link Function} and return a new {@link Result} as either a
     * {@code success} or a {@code failure} depending on the outcome of the {@link Function}.
     *
     * @param exceptionClass the {@link Class} predicate used to determine if the {@link Function} should be applied
     *                       to the {@code failure} or not.
     * @param function       the {@link Function} to apply.
     * @param <X>            the type of {@link Exception} that is mapped by the {@link Function}.
     * @return a new {@link Result} to which the {@link Function} might have been applied.
     * @throws NullPointerException if the {@code exceptionClass} is {@code null},
     *                              if the {@link Function} is {@code null} or
     *                              if the {@link Result} is currently a {@code failure} and the {@link Function}
     *                              is applied and it returns {@code null}.
     * @since 1.0
     */
    <X extends Exception> Result<T> recover(Class<X> exceptionClass, Function<? super X, ? extends T> function);

    /**
     * If the {@link Result} is currently a {@code success} do not apply the {@link OptionalFunction} and return a new
     * {@link Result} as a {@code success} containing the value of the current {@code success}.
     * <p>
     * If the {@link Result} is currently a {@code failure} and the {@link Exception} in the {@code failure} cannot
     * be cast to the {@code exceptionClass} do not apply the {@link OptionalFunction} and return a new {@link Result}
     * as a {@code failure} containing the {@link Exception} of the current {@code failure}.
     * <p>
     * If the {@link Result} is currently a {@code failure} and the {@link Exception} in the {@code failure} can be
     * cast to the {@code exceptionClass} apply the {@link OptionalFunction} and return a new {@link Result} as either
     * a {@code success} or a {@code failure} depending on the outcome of the {@link OptionalFunction}.
     *
     * @param exceptionClass the {@link Class} predicate used to determine if the {@link OptionalFunction} should be
     *                       applied to the {@code failure} or not.
     * @param function       the {@link OptionalFunction} to apply.
     * @param <X>            the type of {@link Exception} that is mapped by the {@link OptionalFunction}.
     * @return a new {@link Result} to which the {@link OptionalFunction} might have been applied.
     * @throws NullPointerException if the {@code exceptionClass} is {@code null},
     *                              if the {@link Function} is {@code null} or
     *                              if the {@link Result} is currently a {@code failure} and the {@link Function}
     *                              is applied and it returns {@code null}.
     * @since 1.0
     */
    <X extends Exception> Result<T> recover(Class<X> exceptionClass, OptionalFunction<? super X, ? extends T> function);

    /**
     * @param exceptionClass
     * @param function
     * @param <X>
     * @return
     * @since 1.0
     */
    <X extends Exception> Result<T> recoverOptional(Class<X> exceptionClass,
                                                    OptionalFunction<? super X, ? extends T> function);

    /**
     * @param exceptionClass
     * @param function
     * @param <X>
     * @return
     * @since 1.0
     */
    <X extends Exception> Result<T> recoverValue(Class<X> exceptionClass, Function<? super X, ? extends T> function);
}
