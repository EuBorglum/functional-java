package eu.borglum.functional.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static eu.borglum.functional.TestDataFactory.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OrElseRecoverTest {

    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("");

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    private static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException("");

    @ParameterizedTest
    @MethodSource("provideOrElseRecover")
    void testOrElseRecover(Result<String> initial, SwitchSupplier<Exception, String> switchSupplier,
                           Result<String> expected) {

        //when
        Result<String> actual = Result.of(() -> initial.orElseRecover(switchSupplier));

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideOrElseRecover() {
        Result<String> illegalArgument = create(ILLEGAL_ARGUMENT_EXCEPTION);
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> runtime = create(RUNTIME_EXCEPTION);
        Result<String> value = create("Value");

        Case<Exception, String> caseIllegalArgument = Case.of(
            ex -> ex instanceof IllegalArgumentException, ex -> "IllegalArgument"
        );

        Case<Exception, String> caseIllegalState = Case.of(
            ex -> ex instanceof IllegalStateException, ex -> "IllegalState"
        );

        Case<Exception, String> caseRuntime = Case.of(
            ex -> ex instanceof RuntimeException, ex -> "Runtime"
        );

        SwitchSupplier<Exception, String> switchIllegalArgumentSupplier = () -> Switch.of(
            caseIllegalArgument
        );

        SwitchSupplier<Exception, String> switchNoCasesSupplier = Switch::of;

        SwitchSupplier<Exception, String> switchRuntimeSupplier = () -> Switch.of(
            caseRuntime
        );

        SwitchSupplier<Exception, String> switchSupplier = () -> Switch.of(
            caseIllegalArgument, caseIllegalState, caseRuntime
        );

        return Stream.of(
            arguments(illegalArgument, switchIllegalArgumentSupplier, create("IllegalArgument")),
            arguments(illegalArgument, switchNoCasesSupplier, illegalArgument),
            arguments(runtime, switchIllegalArgumentSupplier, runtime),
            arguments(illegalArgument, switchRuntimeSupplier, create("Runtime")),
            arguments(illegalArgument, switchSupplier, create("IllegalArgument")),
            arguments(illegalState, switchIllegalArgumentSupplier, illegalState),
            arguments(illegalState, switchSupplier, create("IllegalState")),
            arguments(value, switchNoCasesSupplier, value),
            arguments(value, switchSupplier, value)
        );
    }

    @Test
    void testOrElseRecoverNoValuePresent() {

        //given
        Result<String> initial = create();

        //when
        String actual = initial.orElseRecover(Switch::of);

        //then
        assertNull(actual);
    }

    @ParameterizedTest
    @MethodSource("provideOrElseRecoverInvalid")
    void testOrElseRecoverInvalid(Result<String> initial, SwitchSupplier<Exception, String> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.orElseRecover(invalid));
    }

    private static Stream<Arguments> provideOrElseRecoverInvalid() {
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");

        return Stream.of(
            arguments(illegalState, null),
            arguments(value, null)
        );
    }
}
