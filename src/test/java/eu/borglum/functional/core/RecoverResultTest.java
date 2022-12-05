package eu.borglum.functional.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.FormatterClosedException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static eu.borglum.functional.core.TestDataFactory.create;
import static eu.borglum.functional.core.TestDataFactory.recover;
import static eu.borglum.functional.core.TestDataFactory.recoverOptional;
import static eu.borglum.functional.core.TestDataFactory.recoverOptionalAndThrow;
import static eu.borglum.functional.core.TestDataFactory.recoverToNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class RecoverResultTest {

    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("");

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    @ParameterizedTest
    @MethodSource("provideRecover")
    void testRecover(Result<String> initial, Class<Exception> exceptionClass,
                     Function<? super Exception, ? extends String> recoverFunction, Result<String> expected) {

        //when
        Result<String> actual = initial.recover(exceptionClass, recoverFunction);

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideRecover() {
        Result<String> empty = create();
        Result<String> illegalArgument = create(ILLEGAL_ARGUMENT_EXCEPTION);
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> optional = create(Optional.of("Value"));
        Result<String> recovered = create("Recovered");
        Result<String> value = create("Value");

        Function<? super Exception, ? extends String> failToRecover = TestDataFactory.recover(ILLEGAL_ARGUMENT_EXCEPTION);
        Function<? super Exception, ? extends String> recover = recover("Recovered");

        return Stream.of(
            arguments(value, IllegalStateException.class, recover, value),
            arguments(value, IllegalStateException.class, recover, optional),
            arguments(empty, IllegalStateException.class, recover, empty),
            arguments(illegalState, IllegalStateException.class, recover, recovered),
            arguments(illegalState, Exception.class, recover, recovered),
            arguments(illegalState, FormatterClosedException.class, recover, illegalState),
            arguments(illegalState, NullPointerException.class, recover, illegalState),
            arguments(illegalState, IllegalStateException.class, failToRecover, illegalArgument),
            arguments(illegalArgument, IllegalStateException.class, recover, illegalArgument),
            arguments(illegalArgument, IllegalStateException.class, failToRecover, illegalArgument)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRecoverInvalid")
    void testRecoverInvalid(Result<String> initial, Class<Exception> exceptionClass,
                            Function<? super Exception, ? extends String> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.recover(exceptionClass, invalid));
    }

    private static Stream<Arguments> provideRecoverInvalid() {
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");

        Function<? super Exception, ? extends String> failToRecover = TestDataFactory.recover(ILLEGAL_ARGUMENT_EXCEPTION);
        Function<? super Exception, ? extends String> recover = recover("Recovered");

        return Stream.of(
            arguments(value, null, recover),
            arguments(value, null, failToRecover),
            arguments(value, IllegalStateException.class, null),
            arguments(illegalState, null, recover),
            arguments(illegalState, null, failToRecover),
            arguments(illegalState, null, recoverToNull()),
            arguments(illegalState, IllegalStateException.class, null),
            arguments(illegalState, IllegalStateException.class, recoverToNull())
        );
    }

    @ParameterizedTest
    @MethodSource("provideRecoverOptional")
    void testRecover(Result<String> initial, Class<Exception> exceptionClass,
                     OptionalFunction<? super Exception, ? extends String> recoverFunction, Result<String> expected) {

        //when
        Result<String> actual = initial.recover(exceptionClass, recoverFunction);

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideRecoverOptional() {
        Result<String> empty = create();
        Result<String> illegalArgument = create(ILLEGAL_ARGUMENT_EXCEPTION);
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> optional = create(Optional.of("Value"));
        Result<String> recovered = create("Recovered");
        Result<String> value = create("Value");

        OptionalFunction<? super Exception, ? extends String> recover = recoverOptional("Recovered");
        OptionalFunction<? super Exception, ? extends String> failToRecover = recoverOptionalAndThrow(ILLEGAL_ARGUMENT_EXCEPTION);

        return Stream.of(
            arguments(value, IllegalStateException.class, recover, value),
            arguments(value, IllegalStateException.class, recover, optional),
            arguments(empty, IllegalStateException.class, recover, empty),
            arguments(illegalState, IllegalStateException.class, recover, recovered),
            arguments(illegalState, Exception.class, recover, recovered),
            arguments(illegalState, FormatterClosedException.class, recover, illegalState),
            arguments(illegalState, NullPointerException.class, recover, illegalState),
            arguments(illegalState, IllegalStateException.class, failToRecover, illegalArgument),
            arguments(illegalArgument, IllegalStateException.class, recover, illegalArgument),
            arguments(illegalArgument, IllegalStateException.class, failToRecover, illegalArgument)
        );
    }
}
