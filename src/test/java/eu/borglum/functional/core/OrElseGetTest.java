package eu.borglum.functional.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static eu.borglum.functional.core.TestDataFactory.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OrElseGetTest {

    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("");

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    private static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException("");

    @ParameterizedTest
    @MethodSource("provideOrElseGet")
    void testOrElseGet(Result<String> initial, Supplier<Switch<Exception, String>> switchSupplier,
                       Result<String> expected) {

        //when
        Result<String> actual = Result.of(() -> initial.orElseGet(switchSupplier));

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideOrElseGet() {
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

        Supplier<Switch<Exception, String>> switchIllegalArgumentSupplier = () -> Switch.of(
            Collections.singletonList(caseIllegalArgument)
        );

        Supplier<Switch<Exception, String>> switchNoCasesSupplier = () -> Switch.of(
            Collections.emptyList()
        );

        Supplier<Switch<Exception, String>> switchRuntimeSupplier = () -> Switch.of(
            Collections.singletonList(caseRuntime)
        );

        Supplier<Switch<Exception, String>> switchSupplier = () -> Switch.of(
            Arrays.asList(caseIllegalArgument, caseIllegalState, caseRuntime)
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
    void testOrElseGetNoValuePresent() {

        //given
        Result<String> initial = create();

        //when
        String actual = initial.orElseGet(() -> Switch.of(Collections.emptyList()));

        //then
        assertNull(actual);
    }

    @ParameterizedTest
    @MethodSource("provideOrElseGetInvalid")
    void testOrElseGetInvalid(Result<String> initial, Supplier<Switch<Exception, String>> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.orElseGet(invalid));
    }

    private static Stream<Arguments> provideOrElseGetInvalid() {
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");

        return Stream.of(
            arguments(illegalState, null),
            arguments(value, null)
        );
    }
}
