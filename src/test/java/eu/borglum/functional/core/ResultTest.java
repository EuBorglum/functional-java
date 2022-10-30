package eu.borglum.functional.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ResultTest {

    private static final Result<String> EMPTY_RESULT = Result.of((OptionalSupplier<String>) Optional::empty);

    private static final Result<String> EXCEPTION_OPTIONAL_RESULT = Result.of(() -> Optional.of(
        ((Supplier<String>) () -> {
            throw new RuntimeException("");
        }).get()
    ));

    private static final Function<String, Result<String>> FLAT_MAP_AND_THROW_EXCEPTION = str -> {
        throw new RuntimeException("");
    };

    private static final Function<String, Result<String>> FLAT_MAP_TO_UPPER_CASE =
        str -> Result.of((Supplier<String>) str::toUpperCase);

    private static final Result<String> HELLO_WORLD_RESULT = Result.of(() -> "Hello world");

    private static final Result<String> HELLO_WORLD_OPTIONAL_RESULT = Result.of(() -> Optional.of("Hello world"));

    private static final UnaryOperator<String> MAP_AND_THROW_EXCEPTION = str -> {
        throw new RuntimeException("");
    };

    private static final UnaryOperator<String> MAP_TO_UPPER_CASE = String::toUpperCase;

    private static final Supplier<Result<String>> SUPPLY_EXCEPTION_RESULT = () ->
        Result.of((Supplier<String>) () -> {
            throw new RuntimeException(UUID.randomUUID().toString());
        });

    @ParameterizedTest
    @MethodSource("provideTestFlatMap")
    void testFlatMap(Result<String> initial, Result<String> expected, boolean isEqual) {

        //when
        Result<String> actual = initial.flatMap(str -> Result.of((Supplier<String>) str::toUpperCase));

        //then
        assertEquals(isEqual, expected.equals(actual));
    }

    private static Stream<Arguments> provideTestFlatMap() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT, false),
            arguments(HELLO_WORLD_RESULT, Result.of(() -> "HELLO WORLD"), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.flatMap(FLAT_MAP_TO_UPPER_CASE), true),
            arguments(EMPTY_RESULT, EMPTY_RESULT.flatMap(FLAT_MAP_TO_UPPER_CASE), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.flatMap(FLAT_MAP_AND_THROW_EXCEPTION), false),
            arguments(
                SUPPLY_EXCEPTION_RESULT.get(), SUPPLY_EXCEPTION_RESULT.get().flatMap(FLAT_MAP_TO_UPPER_CASE), false
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFlatMapInvalid")
    void testFlatMapInvalid(Result<String> initialResult, Function<String, Result<String>> invalidFunction) {

        //then
        assertThrows(NullPointerException.class, () -> initialResult.flatMap(invalidFunction));
    }

    private static Stream<Arguments> provideFlatMapInvalid() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, null),
            arguments(HELLO_WORLD_RESULT, (Function<String, Result<String>>) str -> null),
            arguments(EXCEPTION_OPTIONAL_RESULT, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestMap")
    void testMap(Result<String> initial, Result<String> expected, boolean isEqual) {

        //when
        Result<String> actual = initial.map(MAP_TO_UPPER_CASE);

        //then
        assertEquals(isEqual, expected.equals(actual));
    }

    private static Stream<Arguments> provideTestMap() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT, false),
            arguments(HELLO_WORLD_RESULT, Result.of(() -> "HELLO WORLD"), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.map(MAP_TO_UPPER_CASE), true),
            arguments(EMPTY_RESULT, EMPTY_RESULT.map(MAP_TO_UPPER_CASE), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.map(MAP_AND_THROW_EXCEPTION), false),
            arguments(SUPPLY_EXCEPTION_RESULT.get(), SUPPLY_EXCEPTION_RESULT.get().map(MAP_TO_UPPER_CASE), false),
            arguments(SUPPLY_EXCEPTION_RESULT.get(), SUPPLY_EXCEPTION_RESULT.get().map(str -> null), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMapInvalid")
    void testMapInvalid(Result<String> initialResult, UnaryOperator<String> invalidFunction) {

        //then
        assertThrows(NullPointerException.class, () -> initialResult.map(invalidFunction));
    }

    private static Stream<Arguments> provideMapInvalid() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, null),
            arguments(HELLO_WORLD_RESULT, (UnaryOperator<String>) str -> null),
            arguments(EXCEPTION_OPTIONAL_RESULT, null)
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
            arguments(SUPPLY_EXCEPTION_RESULT.get(), false),
            arguments(EXCEPTION_OPTIONAL_RESULT, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOfInvalid")
    void testOfInvalid(Supplier<Result<String>> invalidResult) {

        //then
        assertThrows(NullPointerException.class, invalidResult::get);
    }

    private static Stream<Arguments> provideOfInvalid() {
        return Stream.of(
            arguments((Supplier<Result<String>>) () -> Result.of((Supplier<String>) null)),
            arguments((Supplier<Result<String>>) () -> Result.of((Supplier<String>) () -> null)),
            arguments((Supplier<Result<String>>) () -> Result.of((OptionalSupplier<String>) null)),
            arguments((Supplier<Result<String>>) () -> Result.of((OptionalSupplier<String>) () -> null))
        );
    }
}