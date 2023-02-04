package eu.borglum.functional.core;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @param <T>
 */
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
     * @param function the {@link Function} to apply.
     * @param <U>      the type of the value of the {@link Result} returned by the {@link Function}.
     * @return a new {@link Result} after the {@link Function} has been applied.
     * @throws NullPointerException if the {@link Function} is {@code null} or if the {@link Result} is
     *                              currently a {@code success} and the {@link Function} returns {@code null}.
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
     * If the {@link Result} is currently a {@code success} apply the first {@link Case} of the {@link SwitchSupplier}
     * that matches the value of the {@code success} and return a new {@link Result} as either a {@code success} or
     * a {@code failure} depending on the outcome of the {@link Case} that has been applied.
     * <p>
     * If the {@link SwitchSupplier} does not contain any {@link Case} that can match the value of the {@code success}
     * then a {@link CaseNotFoundException} is thrown.
     * <p>
     * If the {@link Result} is currently a {@code failure} do not apply the {@link SwitchSupplier} and return a new
     * {@link Result} as a {@code failure} containing the {@link Exception} of the current {@code failure}.
     *
     * @param supplier the {@link SwitchSupplier} to apply.
     * @param <U>      the type of value returned by the {@link SwitchSupplier}.
     * @return a new {@link Result} to which the {@link SwitchSupplier} might have been applied.
     * @throws CaseNotFoundException if the {@link SwitchSupplier} is applied but cannot match the value.
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
     * Apply the {@link ValueSupplier} and create a {@link Result} as either a {@code success} or {@code failure}
     * depending on the outcome of the {@link ValueSupplier}.
     *
     * @param supplier the {@link ValueSupplier} to apply.
     * @param <U>      the type of the value returned by the {@link ValueSupplier}.
     * @return the {@link Result} of applying the {@link ValueSupplier}.
     * @throws NullPointerException if the {@link ValueSupplier} is {@code null} or if the {@link ValueSupplier}
     *                              returns {@code null}.
     * @since 1.0
     */
    static <U> Result<U> of(ValueSupplier<U> supplier) {
        return ofValue(supplier);
    }

    /**
     * A convenience method that does the same as {@link #of(OptionalSupplier)}. It might be used if the
     * {@code supplier} is passed as a reference of type {@link Supplier} or to avoid casting if the
     * {@code supplier} is defined as a method reference.
     *
     * @param supplier the {@link Supplier} to apply.
     * @param <U>      the type of the value of the {@link Optional} returned by the {@link Supplier}.
     * @return the {@link Result} of applying the {@link Supplier}.
     * @throws NullPointerException if the {@link Supplier} is {@code null} or if the {@link Supplier}
     *                              returns {@code null}.
     * @since 1.0
     */
    static <U> Result<U> ofOptional(Supplier<Optional<U>> supplier) {
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
     * A convenience method that does the same as {@link #of(ValueSupplier)}. It might be used if the {@code supplier}
     * is passed as a reference of type {@link Supplier} or to avoid casting if the {@code supplier} is defined as a
     * method reference.
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
     * If the {@link Result} is currently a {@code success} do not apply the {@link SwitchSupplier} and return the
     * value.
     * <p>
     * If the {@link Result} is currently a {@code failure} the apply the first {@link Case} of the
     * {@link SwitchSupplier} that matches the cause of the {@code failure} and return the recovered value.
     * <p>
     * If the {@link SwitchSupplier} does not contain any {@link Case} that can match the cause of the {@code failure}
     * then the cause of the {@code failure} is thrown.
     * <p>
     * If the {@link SwitchSupplier} throws an exception while trying to recover then that exception is thrown.
     *
     * @param supplier the {@link SwitchSupplier} to apply.
     * @return the value of the {@code success} or the recovered value of the {@code failure}.
     * @throws NullPointerException if the {@link SwitchSupplier} is {@code null}.
     * @since 1.0
     */
    T orElseRecover(SwitchSupplier<Exception, T> supplier);

    /**
     * If the {@link Result} is currently a {@code success} return the value. If the {@link Result} is currently
     * a {@code failure} throw the exception that is the cause of the {@code failure}
     *
     * @return the value of the {@code success}.
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
     * A convenience method that does the same as {@link #recover(Class, OptionalFunction)}. It might be used to avoid
     * casting the {@code function} to an {@link OptionalFunction} if defined inline as a lambda expression.
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
    <X extends Exception> Result<T> recoverOptional(Class<X> exceptionClass,
                                                    OptionalFunction<? super X, ? extends T> function);

    /**
     * A convenience method that does the same as {@link #recover(Class, Function)}. It might be use to avoid
     * casting the {@code function} to a {@link Function} if defined inline as a lambda expression.
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
    <X extends Exception> Result<T> recoverValue(Class<X> exceptionClass, Function<? super X, ? extends T> function);
}
