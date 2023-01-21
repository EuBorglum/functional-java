package eu.borglum.functional.core;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Case<T, R> {

    private final Predicate<T> predicate;

    private final Function<? super T, ? extends R> function;

    private Case(Predicate<T> predicate, Function<? super T, ? extends R> function) {

        this.predicate = predicate;

        this.function = function;
    }

    /**
     * @param predicate
     * @param function
     * @param <U>
     * @param <V>
     * @return
     */
    public static <U, V> Case<U, V> of(Predicate<U> predicate, Function<? super U, ? extends V> function) {

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
