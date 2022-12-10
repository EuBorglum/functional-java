package eu.borglum.functional.core;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * @param stream
     * @param <T>
     * @return
     * @since 1.0
     */
    static <T> Result<Stream<T>> sequence(Stream<Result<T>> stream) {
        Objects.requireNonNull(stream);

        return Result.of(
            () -> stream
                .map(InternalResult::of)
                .map(
                    result -> result
                        .getValue()
                        .orElse(null)
                )
        );
    }
}
