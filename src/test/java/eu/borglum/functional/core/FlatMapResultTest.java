package eu.borglum.functional.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static eu.borglum.functional.core.TestDataFactory.create;
import static eu.borglum.functional.core.TestDataFactory.createAndThrow;
import static eu.borglum.functional.core.TestDataFactory.flatMapAndThrow;
import static eu.borglum.functional.core.TestDataFactory.flatMapOf;
import static eu.borglum.functional.core.TestDataFactory.flatMapToNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class FlatMapResultTest {

    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("");

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    @ParameterizedTest
    @MethodSource("provideFlatMap")
    void testFlatMap(Result<String> initial,
                     Function<? super String, ? extends Result<? extends String>> flatMapFunction,
                     Result<String> expected) {

        //when
        Result<String> actual = initial.flatMap(flatMapFunction);

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideFlatMap() {
        Result<String> empty = create();
        Result<String> illegalState = createAndThrow(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");
        Result<String> valueUpperCase = create("VALUE");

        Function<? super String, ? extends Result<? extends String>> toUpperCase = str ->
            Result.of((Supplier<String>) str::toUpperCase);

        return Stream.of(
            arguments(empty, flatMapOf(), empty),
            arguments(empty, toUpperCase, empty),
            arguments(illegalState, flatMapAndThrow(ILLEGAL_ARGUMENT_EXCEPTION), illegalState),
            arguments(illegalState, flatMapOf(), illegalState),
            arguments(illegalState, flatMapToNull(), illegalState),
            arguments(illegalState, toUpperCase, illegalState),
            arguments(value, flatMapOf(), value),
            arguments(value, toUpperCase, valueUpperCase),
            arguments(value, flatMapAndThrow(ILLEGAL_STATE_EXCEPTION), illegalState)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFlatMapInvalid")
    void testFlatMapInvalid(Result<String> initial,
                            Function<? super String, ? extends Result<? extends String>> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.flatMap(invalid));
    }

    private static Stream<Arguments> provideFlatMapInvalid() {
        Result<String> illegalState = createAndThrow(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");

        return Stream.of(
            arguments(illegalState, null),
            arguments(value, null),
            arguments(value, flatMapToNull())
        );
    }
}
