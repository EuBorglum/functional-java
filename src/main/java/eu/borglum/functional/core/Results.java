package eu.borglum.functional.core;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Results {

    private Results() {
        //does nothing
    }

    /**
     * @param results
     * @param <T>
     * @return
     * @since 1.0
     */
    static <T> Result<List<T>> sequence(List<Result<T>> results) {
        Objects.requireNonNull(results);

        return Result.of(
            () -> results
                .stream()
                .map(InternalResult::of)
                .map(
                    result -> result
                        .getValue()
                        .orElse(null)
                )
                .collect(Collectors.toList())
        );
    }
}
