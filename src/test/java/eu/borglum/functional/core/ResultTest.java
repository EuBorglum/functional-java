package eu.borglum.functional.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ResultTest {

    @ParameterizedTest
    @MethodSource("provideTestOf")
    void testOf(Result<String> expected, boolean isEqual) {

        //when
        Result<String> subject = Result.of(() -> "Hello world");

        //then
        assertEquals(isEqual, expected.equals(subject));
    }

    @ParameterizedTest
    @MethodSource("provideTestOf")
    void testOfOptional(Result<String> expected, boolean isEqual) {

        //when
        Result<String> subject = Result.of(() -> Optional.of("Hello world"));

        //then
        assertEquals(isEqual, expected.equals(subject));
    }

    private static Stream<Arguments> provideTestOf() {
        return Stream.of(
            arguments(Result.of(() -> "Hello world"), true),
            arguments(Result.of(() -> Optional.of("Hello world")), true),
            arguments(Result.of(() -> "Other"), false),
            arguments(Result.of(() -> Optional.of("Other")), false),
            arguments(Result.of((OptionalSupplier<Object>) Optional::empty), false),
            arguments(
                Result.of(() -> {
                    throw new RuntimeException("");
                }),
                false
            )
        );


    }
}