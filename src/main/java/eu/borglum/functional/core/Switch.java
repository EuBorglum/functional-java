package eu.borglum.functional.core;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link Switch} consists of a {@link List} of {@link Case}s and provides a functional approach similar to that
 * of a {@code switch} statement.
 * <p>
 * When a value is passed to the {@link Switch} for evaluation it will delegate evaluation to the {@link List} of
 * {@link Case}s starting with the first {@link Case} in the {@link List}.
 * <p>
 * if the evaluation of the value is not accepted by the {@link Case} the {@link Switch} will then pass on the
 * value for evaluation to the next {@link Case} in the {@link List}.
 * <p>
 * If the evaluation of the value is accepted by the {@link Case} the {@link Switch} will then pass on the value
 * to be processed by the {@link Case}. The value returned by the {@link Case} will then be returned by the
 * {@link Switch}.
 * <p>
 * Once a value has been accepted by a {@link Case} the remaining {@link Case}s of the {@link Switch} will not be
 * invoked.
 *
 * @param <T> the type of value to be evaluated by the {@link Switch}.
 * @param <R> the type of value to be returned by the {@link Switch}.
 */
public final class Switch<T, R> {

    private final List<Case<T, R>> cases;

    private Switch(List<Case<T, R>> cases) {

        this.cases = cases;
    }

    /**
     * Create a {@link Switch} based on the specified {@link Case}s.
     *
     * @param cases the {@link Case}s that will be used to evaluate values.
     * @param <U>   the type of value to be evaluated by {@link Case}s of the the {@link Switch}.
     * @param <V>   the type of value to be returned by a {@link Case} of the {@link Switch}.
     * @return the {@link Switch} that has been created.
     * @throws NullPointerException if any {@link Case}s is {@code null}.
     * @since 1.0
     */
    @SafeVarargs
    public static <U, V> Switch<U, V> of(Case<U, V>... cases) {

        return new Switch<>(
            Stream
                .of(cases)
                .map(Objects::requireNonNull)
                .collect(Collectors.toList())
        );
    }

    /**
     * Evaluate the {@code value} and return an {@link Optional} as the result of the evaluation.
     * <p>
     * If the {@code value} is not accepted by any {@link Case} then an emoty {@link Optional} is returned.
     *
     * @param value the type of value to evaluate.
     * @return the result of the evaluation.
     * @since 1.0
     */
    public Optional<R> evaluateAsOptional(T value) {

        return findCase(value)
            .map(c -> c.apply(value));
    }

    /**
     * Evaluate the {@code value} and return a {@link Result} as the result of the evaluation.
     *
     * @param value the type of {@code value} to evaluate.
     * @return the result of the evaluation.
     * @throws CaseNotFoundException if the value is not accepted by any {@link Case}.
     * @since 1.0
     */
    public Result<R> evaluateStrictAsResult(T value) {

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
