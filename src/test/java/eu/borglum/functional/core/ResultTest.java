package eu.borglum.functional.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ResultTest {

    private static final Result<String> EMPTY_RESULT = Result.of((OptionalSupplier<String>) Optional::empty);

    private static final UnaryOperator<String> EXCEPTION_MAPPER = str -> {
        throw new RuntimeException("");
    };

    private static final Result<String> EXCEPTION_RESULT = Result.of(() -> {
        throw new RuntimeException("");
    });

    private static final Result<String> EXCEPTION_OPTIONAL_RESULT = Result.of(() -> Optional.of(
        ((Supplier<String>) () -> {
            throw new RuntimeException("");
        }).get()
    ));

    private static final Result<String> HELLO_WORLD_RESULT = Result.of(() -> "Hello world");

    private static final Result<String> HELLO_WORLD_OPTIONAL_RESULT = Result.of(() -> Optional.of("Hello world"));

    private static final UnaryOperator<String> UPPER_CASE_MAPPER = String::toUpperCase;

    @ParameterizedTest
    @MethodSource("provideTestMap")
    void testMap(Result<String> initial, Result<String> expected, boolean isEqual) {

        //when
        Result<String> actual = initial.map(UPPER_CASE_MAPPER);

        //then
        assertEquals(isEqual, expected.equals(actual));
    }

    private static Stream<Arguments> provideTestMap() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT, false),
            arguments(HELLO_WORLD_RESULT, Result.of(() -> "HELLO WORLD"), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.map(UPPER_CASE_MAPPER), true),
            arguments(EMPTY_RESULT, EMPTY_RESULT.map(UPPER_CASE_MAPPER), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.map(EXCEPTION_MAPPER), false),
            arguments(EXCEPTION_RESULT, EXCEPTION_RESULT.map(UPPER_CASE_MAPPER), true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestOf")
    void testOf(Result<String> expected, boolean isEqual) {

        //then
        assertEquals(isEqual, expected.equals(HELLO_WORLD_RESULT));
    }

    @ParameterizedTest
    @MethodSource("provideTestOf")
    void testOfOptional(Result<String> expected, boolean isEqual) {

        //then
        assertEquals(isEqual, expected.equals(HELLO_WORLD_OPTIONAL_RESULT));
    }

    private static Stream<Arguments> provideTestOf() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, true),
            arguments(HELLO_WORLD_OPTIONAL_RESULT, true),
            arguments(Result.of(() -> "Other"), false),
            arguments(Result.of(() -> Optional.of("Other")), false),
            arguments(EMPTY_RESULT, false),
            arguments(EXCEPTION_RESULT, false),
            arguments(EXCEPTION_OPTIONAL_RESULT, false)
        );


    }
}