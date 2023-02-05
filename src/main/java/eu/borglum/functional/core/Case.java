package eu.borglum.functional.core;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @param <T>
 * @param <R>
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
     * @param predicate the {@link Predicate} to determine if the {@link Function} should be applied.
     * @param function  the {@link Function} to apply if the {@link Predicate} evaluates to {@code true}.
     * @param <U>       the type of value to be evaluated by the {@link Predicate} and applied to the
     *                  {@link Function}.
     * @param <V>       the type of value to be returned by the {@link Function}.
     * @return the {@link Case} that has been created
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
