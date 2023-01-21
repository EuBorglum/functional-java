package eu.borglum.functional.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static eu.borglum.functional.core.TestDataFactory.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class FilterResultTest {

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    @ParameterizedTest
    @MethodSource("provideFilter")
    void testFilter(Result<String> initial, Predicate<? super String> predicate, Result<String> expected) {

        //when
        Result<String> actual = initial.filter(predicate);

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideFilter() {
        Result<String> empty = create();
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");

        Predicate<? super String> acceptAll = str -> true;
        Predicate<? super String> acceptNone = str -> false;

        return Stream.of(
            arguments(empty, acceptAll, empty),
            arguments(empty, acceptNone, empty),
            arguments(illegalState, acceptAll, illegalState),
            arguments(illegalState, acceptNone, illegalState),
            arguments(value, acceptAll, value),
            arguments(value, acceptNone, empty)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilterInvalid")
    void testFilterInvalid(Result<String> initial, Predicate<? super String> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.filter(invalid));
    }

    private static Stream<Arguments> provideFilterInvalid() {
        Result<String> value = create("Value");
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);

        return Stream.of(
            arguments(value, null),
            arguments(illegalState, null)
        );
    }
}
