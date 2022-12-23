package eu.borglum.functional.core;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class Switch<T, R> {

    private final List<Case<T, R>> cases;

    private final Function<? super T, ? extends R> defaultCase;

    private Switch(List<Case<T, R>> cases, Function<? super T, ? extends R> defaultCase) {

        this.cases = cases;

        this.defaultCase = defaultCase;
    }

    public static <U, V> Switch<U, V> of(List<Case<U, V>> cases) {

        Function<? super U, ? extends V> throwNoDefaultCaseException = u -> {
            throw new NoDefaultCaseException("No default case has been specified");
        };

        return of(cases, throwNoDefaultCaseException);
    }

    /**
     * @param cases
     * @param defaultCase
     * @param <U>
     * @param <V>
     * @return
     */
    public static <U, V> Switch<U, V> of(List<Case<U, V>> cases, Function<? super U, ? extends V> defaultCase) {

        Objects.requireNonNull(cases);

        Objects.requireNonNull(defaultCase);

        return new Switch<>(cases, defaultCase);
    }

    R evaluate(T value) {

        return cases
            .stream()
            .filter(c -> c.accept(value))
            .findFirst()
            .map(c -> c.apply(value))
            .orElseGet(() -> defaultCase.apply(value));
    }
}
