package eu.borglum.functional.core;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Switch<T, R> {

    private final List<Case<T, R>> cases;

    private Switch(List<Case<T, R>> cases) {

        this.cases = cases;
    }

    /**
     * @param cases
     * @param <U>
     * @param <V>
     * @return
     * @since 1.0
     */
    public static <U, V> Switch<U, V> of(List<Case<U, V>> cases) {

        Objects.requireNonNull(cases);

        return new Switch<>(cases);
    }

    Optional<R> evaluate(T value) {

        return cases
            .stream()
            .filter(c -> c.accept(value))
            .findFirst()
            .map(c -> c.apply(value));
    }
}
