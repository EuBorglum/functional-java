package eu.borglum.functional.core;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A {@link Case} consists of a {@link Predicate} and a {@link Function} and provides a functional approach similar
 * to that of a {@code case} expression in a {@code switch} statement.
 * <p>
 * A value should first be passed to the {@link Predicate} for evaluation to determine if the value is accepted by the
 * {@link Case}. If the evaluation results in the value being accepted by the {@link Case}, i.e. the {@link Predicate}
 * evaluates to {@code true} then it is safe to apply the value to the {@link Function}.
 *
 * @param <T> the type of value to be evaluated and potentially processed by the {@link Case}.
 * @param <R> the type of value to be returned by the {@link Case}.
 */
public final class Case<T, R> {

    private final Predicate<? super T> predicate;

    private final Function<? super T, ? extends R> function;

    private Case(Predicate<? super T> predicate, Function<? super T, ? extends R> function) {

        this.predicate = predicate;

        this.function = function;
    }

    /**
     * Create a {@link Case} based on the specified {@link Predicate} and {@link Function}.
     *
     * @param predicate the {@link Predicate} to determine if a value is accepted when being evaluated by the
     *                  {@link Case}.
     * @param function  the {@link Function} to apply to a value that has been evaluated and accepted by the
     *                  {@link Case}.
     * @param <U>       the type of value to be evaluated by the {@link Predicate} and applied to the
     *                  {@link Function}.
     * @param <V>       the type of value to be returned by the {@link Function}.
     * @return the {@link Case} that has been created
     * @throws NullPointerException if the {@link Predicate} or the {@link Function} are {@code null}.
     * @since 1.0
     */
    public static <U, V> Case<U, V> of(Predicate<? super U> predicate, Function<? super U, ? extends V> function) {

        Objects.requireNonNull(predicate);

        Objects.requireNonNull(function);

        return new Case<>(predicate, function);
    }

    boolean accept(T value) {

        return predicate.test(value);
    }

    R apply(T value) {

        return function.apply(value);
    }
}
