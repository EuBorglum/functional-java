package eu.borglum.functional.core;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @param <T>
 * @param <R>
 */
public final class Case<T, R> {

    private final Predicate<T> matcher;

    private final Function<? super T, ? extends R> mapper;

    private Case(Predicate<T> matcher, Function<? super T, ? extends R> mapper) {

        this.matcher = matcher;

        this.mapper = mapper;
    }

    /**
     * @param matcher
     * @param mapper
     * @param <U>
     * @param <V>
     * @return
     */
    public static <U, V> Case<U, V> of(Predicate<U> matcher, Function<? super U, ? extends V> mapper) {

        Objects.requireNonNull(matcher);

        Objects.requireNonNull(mapper);

        return new Case<>(matcher, mapper);
    }

    boolean accept(T value) {

        return matcher.test(value);
    }

    R apply(T value) {

        return mapper.apply(value);
    }
}
