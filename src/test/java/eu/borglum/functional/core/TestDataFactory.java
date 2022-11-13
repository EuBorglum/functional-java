package eu.borglum.functional.core;

import java.util.Optional;
import java.util.function.Function;

class TestDataFactory {

    static Result<String> create() {
        return Result.of(Optional::empty);
    }

    static Result<String> create(String value) {
        return Result.of(() -> value);
    }

    static Result<String> create(Optional<String> value) {
        return Result.of(() -> value);
    }

    static Result<String> createAndThrow(RuntimeException exception) {
        return Result.of(() -> {
            throw exception;
        });
    }

    static Function<? super String, ? extends Result<? extends String>> flatMapAndThrow(RuntimeException exception) {
        return str -> Result.of(
            () -> {
                throw exception;
            }
        );
    }

    static Function<? super String, ? extends Result<? extends String>> flatMapOf() {
        return str -> Result.of(() -> str);
    }

    static Function<? super String, ? extends Result<? extends String>> flatMapToNull() {
        return str -> null;
    }

    static Function<? super String, ? extends String> mapAndThrow(RuntimeException exception) {
        return str -> {
            throw exception;
        };
    }

    static OptionalFunction<? super String, ? extends String> mapOptionalAndThrow(RuntimeException exception) {
        return str -> {
            throw exception;
        };
    }

    static OptionalFunction<? super String, ? extends String> mapOptionalToNull() {
        return str -> null;
    }

    static Function<? super String, ? extends String> mapToNull() {
        return str -> null;
    }

    static Function<? super Exception, ? extends String> recoverToNull() {
        return ex -> null;
    }

    static Function<? super Exception, ? extends String> recover(String value) {
        return ex -> value;
    }

    static Function<? super Exception, ? extends String> recoverAndThrow(RuntimeException exception) {
        return ex -> {
            throw exception;
        };
    }
}
