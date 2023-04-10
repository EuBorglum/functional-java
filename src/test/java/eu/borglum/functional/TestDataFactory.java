package eu.borglum.functional;

import eu.borglum.functional.core.OptionalFunction;
import eu.borglum.functional.core.Result;
import eu.borglum.functional.core.ValueFunction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataFactory {

    public static Result<String> create() {
        return Result.ofOptional(Optional::empty);
    }

    public static  Result<String> create(String value) {
        return Result.of(() -> value);
    }

    public static  Result<String> create(Optional<String> value) {
        return Result.ofOptional(() -> value);
    }

    public static  Result<String> create(RuntimeException exception) {
        return Result.ofOptional(() -> {
            throw exception;
        });
    }

    public static  Result<List<String>> create(List<String> values) {
        return Result.of(() -> values);
    }

    public static  Result<Set<String>> create(Set<String> values) {
        return Result.of(() -> values);
    }

    public static  List<Result<String>> createList(Collection<String> values) {
        return createStream(values).collect(Collectors.toList());
    }

    public static  List<Result<String>> createList(RuntimeException exception, Collection<String> values) {
        return Stream
            .concat(
                Stream
                    .of(exception)
                    .map(TestDataFactory::create),
                createStream(values)
            )
            .collect(Collectors.toList());
    }

    public static  List<Result<String>> createList(Collection<String> values, RuntimeException exception) {
        return Stream
            .concat(
                createStream(values),
                Stream
                    .of(exception)
                    .map(TestDataFactory::create)
            )
            .collect(Collectors.toList());
    }

    public static  List<Result<String>> createListOptional(Collection<String> values) {
        return createStreamOptional(values).collect(Collectors.toList());
    }

    public static  Set<Result<String>> createSet(Collection<String> values) {
        return createStream(values).collect(Collectors.toSet());
    }

    public static  Set<Result<String>> createSet(RuntimeException exception, Collection<String> values) {
        return Stream
            .concat(
                Stream
                    .of(exception)
                    .map(TestDataFactory::create),
                createStream(values)
            )
            .collect(Collectors.toSet());
    }

    public static  Set<Result<String>> createSet(Collection<String> values, RuntimeException exception) {
        return Stream
            .concat(
                createStream(values),
                Stream
                    .of(exception)
                    .map(TestDataFactory::create)
            )
            .collect(Collectors.toSet());
    }

    public static  Set<Result<String>> createSetOptional(Collection<String> values) {
        return createStreamOptional(values).collect(Collectors.toSet());
    }

    public static  Stream<Result<String>> createStream(Collection<String> values) {
        return values
            .stream()
            .map(TestDataFactory::create);
    }

    public static  Stream<Result<String>> createStreamOptional(Collection<String> values) {
        return values
            .stream()
            .map(Optional::ofNullable)
            .map(TestDataFactory::create);
    }

    public static  Function<? super String, ? extends Result<? extends String>> flatMapAndThrowInResult
        (RuntimeException exception) {

        return str -> Result.ofOptional(
            () -> {
                throw exception;
            }
        );
    }

    public static  Function<? super String, ? extends Result<? extends String>> flatMapAndThrowOutsideOfResult
        (RuntimeException exception) {

        return str -> {
            throw exception;
        };
    }

    public static  Function<? super String, ? extends Result<? extends String>> flatMapOf() {
        return str -> Result.of(() -> str);
    }

    public static  Function<? super String, ? extends Result<? extends String>> flatMapToNull() {
        return str -> null;
    }

    public static ValueFunction<? super String, ? extends String> mapAndThrow(RuntimeException exception) {
        return str -> {
            throw exception;
        };
    }

    public static  Function<? super Exception, ? extends Exception> mapFailure(RuntimeException exception) {
        return ex -> exception;
    }

    public static  Function<? super Exception, ? extends Exception> mapFailureAndThrow(RuntimeException exception) {
        return ex -> {
            throw exception;
        };
    }

    public static  Function<? super Exception, ? extends Exception> mapFailureToNull() {
        return ex -> null;
    }

    public static OptionalFunction<? super String, ? extends String> mapOptionalAndThrow(RuntimeException exception) {
        return str -> {
            throw exception;
        };
    }

    public static  OptionalFunction<? super String, ? extends String> mapOptionalToNull() {
        return str -> null;
    }

    public static  ValueFunction<? super String, ? extends String> mapToNull() {
        return str -> null;
    }

    public static  ValueFunction<? super Exception, ? extends String> recoverToNull() {
        return ex -> null;
    }

    public static  ValueFunction<? super Exception, ? extends String> recover(String value) {
        return ex -> value;
    }

    public static  ValueFunction<? super Exception, ? extends String> recover(RuntimeException exception) {
        return ex -> {
            throw exception;
        };
    }

    public static  OptionalFunction<? super Exception, ? extends String> recoverOptional(String value) {
        return ex -> Optional.ofNullable(value);
    }

    public static  OptionalFunction<? super Exception, ? extends String> recoverOptional(RuntimeException exception) {
        return ex -> {
            throw exception;
        };
    }

    public static  OptionalFunction<? super Exception, ? extends String> recoverOptionalToNull() {
        return ex -> null;
    }
}
