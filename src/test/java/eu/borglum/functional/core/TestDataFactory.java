package eu.borglum.functional.core;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    static Result<String> create(RuntimeException exception) {
        return Result.of(() -> {
            throw exception;
        });
    }

    static Result<List<String>> create(List<String> values) {
        return Result.of(() -> values);
    }

    static List<Result<String>> createList(List<String> values) {
        return values
            .stream()
            .map(TestDataFactory::create)
            .collect(Collectors.toList());
    }

    static List<Result<String>> createList(RuntimeException exception, List<String> values) {
        return Stream
            .concat(
                Stream
                    .of(exception)
                    .map(TestDataFactory::create),
                values
                    .stream()
                    .map(TestDataFactory::create)
            )
            .collect(Collectors.toList());
    }

    static List<Result<String>> createList(List<String> values, RuntimeException exception) {
        return Stream
            .concat(
                values
                    .stream()
                    .map(TestDataFactory::create),
                Stream
                    .of(exception)
                    .map(TestDataFactory::create)
            )
            .collect(Collectors.toList());
    }

    static List<Result<String>> createListOptional(List<String> values) {
        return values
            .stream()
            .map(Optional::ofNullable)
            .map(TestDataFactory::create)
            .collect(Collectors.toList());
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

    static Function<? super Exception, ? extends String> recover(RuntimeException exception) {
        return ex -> {
            throw exception;
        };
    }

    static OptionalFunction<? super Exception, ? extends String> recoverOptional(String value) {
        return ex -> Optional.ofNullable(value);
    }

    static OptionalFunction<? super Exception, ? extends String> recoverOptionalAndThrow(RuntimeException exception) {
        return ex -> {
            throw exception;
        };
    }
}
