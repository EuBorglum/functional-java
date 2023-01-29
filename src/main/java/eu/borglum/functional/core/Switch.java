package eu.borglum.functional.core;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @param <T>
 * @param <R>
 */
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
     */
    @SafeVarargs
    public static <U, V> Switch<U, V> of(Case<U, V>... cases) {

        return new Switch<>(Arrays.asList(cases));
    }

    Optional<R> evaluateAsOptional(T value) {

        return findCase(value)
            .map(c -> c.apply(value));
    }

    Result<R> evaluateAsResult(T value) {

        return findCase(value)
            .map(c -> Result.of(() -> c.apply(value)))
            .orElseThrow(() -> new CaseNotFoundException(
                String.format("Unable to find case to apply to the value '%s'", value)
            ));
    }

    private Optional<Case<T, R>> findCase(T value) {

        return cases
            .stream()
            .filter(c -> c.accept(value))
            .findFirst();
    }
}
