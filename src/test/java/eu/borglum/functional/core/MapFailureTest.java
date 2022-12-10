package eu.borglum.functional.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static eu.borglum.functional.core.TestDataFactory.create;
import static eu.borglum.functional.core.TestDataFactory.mapFailure;
import static eu.borglum.functional.core.TestDataFactory.mapFailureAndThrow;
import static eu.borglum.functional.core.TestDataFactory.mapFailureToNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MapFailureTest {

    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("");

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    private static final NumberFormatException NUMBER_FORMAT_EXCEPTION = new NumberFormatException("");

    @ParameterizedTest
    @MethodSource("provideMapFailure")
    void testMapFailure(Result<String> initial, Class<Exception> exceptionClass,
                        Function<? super Exception, ? extends Exception> function, Result<String> expected) {

        //when
        Result<String> actual = initial
            .mapFailure(NumberFormatException.class, ex -> NUMBER_FORMAT_EXCEPTION)
            .mapFailure(exceptionClass, function);

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideMapFailure() {
        Result<String> empty = create();
        Result<String> illegalArgument = create(ILLEGAL_ARGUMENT_EXCEPTION);
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> numberFormat = create(NUMBER_FORMAT_EXCEPTION);

        Function<? super Exception, ? extends Exception> map = mapFailure(ILLEGAL_STATE_EXCEPTION);
        Function<? super Exception, ? extends Exception> failToMap = mapFailureAndThrow(ILLEGAL_ARGUMENT_EXCEPTION);

        return Stream.of(
            arguments(empty, IllegalStateException.class, map, empty),
            arguments(illegalState, IllegalStateException.class, map, illegalState),
            arguments(illegalState, IllegalStateException.class, failToMap, illegalArgument),
            arguments(illegalState, Exception.class, map, illegalState),
            arguments(illegalArgument, Exception.class, map, illegalState),
            arguments(illegalArgument, NullPointerException.class, map, illegalArgument),
            arguments(illegalArgument, IllegalArgumentException.class, map, illegalState),
            arguments(numberFormat, NullPointerException.class, map, numberFormat)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMapFailureInvalid")
    void testMapFailureInvalid(Result<String> initial, Class<Exception> exceptionClass,
                               Function<? super Exception, ? extends Exception> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.mapFailure(exceptionClass, invalid));
    }

    private static Stream<Arguments> provideMapFailureInvalid() {
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");

        Function<? super Exception, ? extends Exception> map = mapFailure(ILLEGAL_STATE_EXCEPTION);
        Function<? super Exception, ? extends Exception> failToMap = mapFailureAndThrow(ILLEGAL_ARGUMENT_EXCEPTION);

        return Stream.of(
            arguments(illegalState, null, failToMap),
            arguments(illegalState, null, map),
            arguments(illegalState, Exception.class, null),
            arguments(illegalState, Exception.class, mapFailureToNull()),
            arguments(value, null, failToMap),
            arguments(value, null, map),
            arguments(value, Exception.class, null)
        );
    }
}
