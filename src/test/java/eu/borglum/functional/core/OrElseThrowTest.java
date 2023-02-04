package eu.borglum.functional.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static eu.borglum.functional.core.TestDataFactory.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OrElseThrowTest {

    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("");

    @ParameterizedTest
    @MethodSource("provideOrElseThrow")
    void testOrElseThrow(Result<String> initial, Result<String> expected) {

        //when
        Result<String> actual = Result.of(initial::orElseThrow);

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideOrElseThrow() {
        return Stream.of(
            arguments(create("Value"), create("Value")),
            arguments(create(ILLEGAL_ARGUMENT_EXCEPTION), create(ILLEGAL_ARGUMENT_EXCEPTION))
        );
    }

    @Test
    void testOrElseThrowNoValuePresent() {

        //when
        Result<String> actual = Result.ofOptional(Optional::empty);

        //then
        assertNull(actual.orElseThrow());
    }
}
