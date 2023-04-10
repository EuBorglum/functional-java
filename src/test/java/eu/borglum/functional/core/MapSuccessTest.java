package eu.borglum.functional.core;

import eu.borglum.functional.internal.InternalResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static eu.borglum.functional.TestDataFactory.create;
import static eu.borglum.functional.TestDataFactory.mapAndThrow;
import static eu.borglum.functional.TestDataFactory.mapOptionalAndThrow;
import static eu.borglum.functional.TestDataFactory.mapOptionalToNull;
import static eu.borglum.functional.TestDataFactory.mapToNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MapSuccessTest {

    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION = new IllegalArgumentException("");

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    @ParameterizedTest
    @MethodSource("provideMap")
    void testMap(Result<String> initial, ValueFunction<? super String, ? extends String> mapFunction,
                 Result<String> expected) {

        //when
        Result<String> actual = initial.map(mapFunction);

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideMap() {
        Result<String> empty = create();
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");
        Result<String> valueUpperCase = create("VALUE");

        ValueFunction<? super String, ? extends String> identity = str -> str;
        ValueFunction<? super String, ? extends String> toUpperCase = String::toUpperCase;

        return Stream.of(
            arguments(empty, identity, empty),
            arguments(empty, toUpperCase, empty),
            arguments(illegalState, identity, illegalState),
            arguments(illegalState, mapAndThrow(ILLEGAL_ARGUMENT_EXCEPTION), illegalState),
            arguments(illegalState, mapToNull(), illegalState),
            arguments(illegalState, toUpperCase, illegalState),
            arguments(value, identity, value),
            arguments(value, mapAndThrow(ILLEGAL_STATE_EXCEPTION), illegalState),
            arguments(value, toUpperCase, valueUpperCase)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMapInvalid")
    void testMapInvalid(Result<String> initial, ValueFunction<? super String, ? extends String> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.map(invalid));
    }

    private static Stream<Arguments> provideMapInvalid() {
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");

        return Stream.of(
            arguments(illegalState, null),
            arguments(value, null),
            arguments(value, mapToNull())
        );
    }

    @ParameterizedTest
    @MethodSource("provideMapOptional")
    void testMapOptional(Result<String> initial, OptionalFunction<? super String, ? extends String> mapFunction,
                         Result<String> expected) {

        //when
        Result<String> actual = initial.map(mapFunction);

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideMapOptional() {
        Result<String> empty = create();
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");
        Result<String> valueUpperCase = create("VALUE");

        OptionalFunction<? super String, ? extends String> identity = Optional::of;
        OptionalFunction<? super String, ? extends String> toUpperCase = str -> Optional.of(str.toUpperCase());
        OptionalFunction<? super String, ? extends String> throwIllegalArgument =
            mapOptionalAndThrow(ILLEGAL_ARGUMENT_EXCEPTION);
        OptionalFunction<? super String, ? extends String> throwIllegalState =
            mapOptionalAndThrow(ILLEGAL_STATE_EXCEPTION);

        return Stream.of(
            arguments(empty, toUpperCase, empty),
            arguments(illegalState, identity, illegalState),
            arguments(illegalState, mapOptionalToNull(), illegalState),
            arguments(illegalState, throwIllegalArgument, illegalState),
            arguments(illegalState, toUpperCase, illegalState),
            arguments(value, identity, value),
            arguments(value, throwIllegalState, illegalState),
            arguments(value, toUpperCase, valueUpperCase)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMapOptionalInvalid")
    void testMapOptionalInvalid(Result<String> initial, OptionalFunction<? super String, ? extends String> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.map(invalid));
    }

    private static Stream<Arguments> provideMapOptionalInvalid() {
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");

        return Stream.of(
            arguments(illegalState, null),
            arguments(value, null),
            arguments(value, mapOptionalToNull())
        );
    }

    @ParameterizedTest
    @MethodSource("provideMapSwitch")
    void testMapSwitch(Result<String> initial, SwitchSupplier<String, String> switchSupplier, Result<String> expected) {

        //when
        Result<String> actual = initial.map(switchSupplier);

        //then
        if (InternalResult.of(expected).isSuccess()) {
            assertEquals(expected, actual);
        } else {
            assertAll(
                () -> assertEquals(toCause(expected).getClass(), toCause(actual).getClass()),
                () -> assertTrue(toCause(actual).getMessage().contains(toCause(expected).getMessage()))
            );
        }
    }

    private static Stream<Arguments> provideMapSwitch() {
        Result<String> value = create("Value");

        Case<String, String> caseValue = Case.of(
            "Value"::equals, String::toUpperCase
        );

        SwitchSupplier<String, String> switchValueSupplier = () -> Switch.of(
            caseValue
        );

        SwitchSupplier<String, String> switchSupplier = () -> Switch.of(
            Case.of("OtherValue"::equals, String::toUpperCase),
            caseValue
        );

        return Stream.of(
            arguments(value, switchValueSupplier, create("VALUE")),
            arguments(value, switchSupplier, create("VALUE"))
        );
    }

    private Exception toCause(Result<String> result) {
        return InternalResult.of(result).getCause();
    }

    @ParameterizedTest
    @MethodSource("provideMapSwitchInvalid")
    void testMapSwitchInvalid(Result<String> initial, SwitchSupplier<String, String> invalid,
                              Class<? extends Exception> exceptionClass) {

        //when
        assertThrows(exceptionClass, () -> initial.map(invalid));
    }

    private static Stream<Arguments> provideMapSwitchInvalid() {
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> value = create("Value");

        SwitchSupplier<String, String> switchToNull = () -> Switch.of(
            Case.of(str -> true, str -> null)
        );

        SwitchSupplier<String, String> switchOtherValueSupplier = () -> Switch.of(
            Case.of("OtherValue"::equals, String::toUpperCase)
        );

        return Stream.of(
            arguments(illegalState, null, NullPointerException.class),
            arguments(value, null, NullPointerException.class),
            arguments(value, switchToNull, NullPointerException.class),
            arguments(value, switchOtherValueSupplier, CaseNotFoundException.class)
        );
    }
}
