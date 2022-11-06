package eu.borglum.functional.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ResultTest {

    private static final RuntimeException FIXED_EXCEPTION = new RuntimeException("");

    private static final Result<String> EMPTY_RESULT = Result.of(Optional::empty);

    private static final Result<String> FIXED_EXCEPTION_OPTIONAL_RESULT = Result.of(() -> Optional.of(
        ((Supplier<String>) () -> {
            throw FIXED_EXCEPTION;
        }).get()
    ));

    private static final Predicate<String> FILTER_AND_THROW_FIXED_EXCEPTION = str -> {
        throw FIXED_EXCEPTION;
    };

    private static final Function<String, Result<String>> FLAT_MAP_AND_THROW_FIXED_EXCEPTION = str -> {
        throw FIXED_EXCEPTION;
    };

    private static final Function<String, Result<String>> FLAT_MAP_TO_UPPER_CASE =
        str -> Result.of((Supplier<String>) str::toUpperCase);

    private static final Result<String> HELLO_WORLD_RESULT = Result.of(() -> "Hello world");

    private static final Result<String> HELLO_WORLD_OPTIONAL_RESULT = Result.of(() -> Optional.of("Hello world"));

    private static final UnaryOperator<String> MAP_AND_THROW_FIXED_EXCEPTION = str -> {
        throw FIXED_EXCEPTION;
    };

    private static final OptionalFunction<String, Optional<String>> MAP_AND_THROW_OPTIONAL_FIXED_EXCEPTION = str -> {
        throw FIXED_EXCEPTION;
    };

    private static final UnaryOperator<String> MAP_TO_UPPER_CASE = String::toUpperCase;

    private static final OptionalUnaryOperator<String> MAP_TO_OPTIONAL_UPPER_CASE =
        str -> Optional.of(str.toUpperCase());

    private static final Supplier<Result<String>> SUPPLY_UNIQUE_EXCEPTION_RESULT = () ->
        Result.of((Supplier<String>) () -> {
            throw new RuntimeException(UUID.randomUUID().toString());
        });

    @ParameterizedTest
    @MethodSource("provideFilter")
    void testFilter(Result<String> initial, Result<String> expected, boolean isEqual) {

        //when
        Result<String> actual = initial.filter("Hello world"::equals);

        //then
        assertEquals(isEqual, expected.equals(actual));
    }

    private static Stream<Arguments> provideFilter() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT, true),
            arguments(HELLO_WORLD_RESULT, EMPTY_RESULT, false),
            arguments(HELLO_WORLD_RESULT.map(MAP_TO_UPPER_CASE), EMPTY_RESULT, true),
            arguments(FIXED_EXCEPTION_OPTIONAL_RESULT, FIXED_EXCEPTION_OPTIONAL_RESULT, true),
            arguments(EMPTY_RESULT, EMPTY_RESULT, true),
            arguments(
                HELLO_WORLD_RESULT.filter(FILTER_AND_THROW_FIXED_EXCEPTION), FIXED_EXCEPTION_OPTIONAL_RESULT, true
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilterInvalid")
    void testFilterInvalid(Result<String> initial, Predicate<String> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.filter(invalid));
    }

    private static Stream<Arguments> provideFilterInvalid() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, null),
            arguments(FIXED_EXCEPTION_OPTIONAL_RESULT, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFlatMap")
    void testFlatMap(Result<String> initial, Result<String> expected, boolean isEqual) {

        //when
        Result<String> actual = initial.flatMap(FLAT_MAP_TO_UPPER_CASE);

        //then
        assertEquals(isEqual, expected.equals(actual));
    }

    private static Stream<Arguments> provideFlatMap() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT, false),
            arguments(HELLO_WORLD_RESULT, Result.of(() -> "HELLO WORLD"), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.flatMap(FLAT_MAP_TO_UPPER_CASE), true),
            arguments(EMPTY_RESULT, EMPTY_RESULT.flatMap(FLAT_MAP_TO_UPPER_CASE), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.flatMap(FLAT_MAP_AND_THROW_FIXED_EXCEPTION), false),
            arguments(
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get(),
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get().flatMap(FLAT_MAP_TO_UPPER_CASE),
                false
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFlatMapInvalid")
    void testFlatMapInvalid(Result<String> initial, Function<String, Result<String>> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.flatMap(invalid));
    }

    private static Stream<Arguments> provideFlatMapInvalid() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, null),
            arguments(HELLO_WORLD_RESULT, (Function<String, Result<String>>) str -> null),
            arguments(FIXED_EXCEPTION_OPTIONAL_RESULT, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMap")
    void testMap(Result<String> initial, Result<String> expected, boolean isEqual) {

        //when
        Result<String> actual = initial.map(MAP_TO_UPPER_CASE);

        //then
        assertEquals(isEqual, expected.equals(actual));
    }

    private static Stream<Arguments> provideMap() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT, false),
            arguments(HELLO_WORLD_RESULT, Result.of(() -> "HELLO WORLD"), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.map(MAP_TO_UPPER_CASE), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.map(MAP_TO_OPTIONAL_UPPER_CASE), true),
            arguments(EMPTY_RESULT, EMPTY_RESULT.map(MAP_TO_UPPER_CASE), true),
            arguments(EMPTY_RESULT, EMPTY_RESULT.map(MAP_TO_OPTIONAL_UPPER_CASE), true),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.map(MAP_AND_THROW_FIXED_EXCEPTION), false),
            arguments(HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.map(MAP_AND_THROW_OPTIONAL_FIXED_EXCEPTION), false),
            arguments(
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get(),
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get().map(MAP_TO_UPPER_CASE),
                false
            ),
            arguments(
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get(),
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get().map(MAP_TO_OPTIONAL_UPPER_CASE),
                false
            ),
            arguments(
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get(),
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get().map((UnaryOperator<String>) str -> null),
                false
            ),
            arguments(
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get(),
                SUPPLY_UNIQUE_EXCEPTION_RESULT.get().map((OptionalFunction<String, Optional<String>>) str -> null),
                false
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideMapInvalid")
    void testMapInvalid(Result<String> initial, UnaryOperator<String> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.map(invalid));
    }

    private static Stream<Arguments> provideMapInvalid() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, null),
            arguments(HELLO_WORLD_RESULT, (UnaryOperator<String>) str -> null),
            arguments(FIXED_EXCEPTION_OPTIONAL_RESULT, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMapOptionalInvalid")
    void testMapOptionalInvalid(Result<String> initial, OptionalFunction<String, String> invalid) {

        //then
        assertThrows(NullPointerException.class, () -> initial.map(invalid));
    }

    private static Stream<Arguments> provideMapOptionalInvalid() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, null),
            arguments(HELLO_WORLD_RESULT, (OptionalFunction<String, String>) str -> null),
            arguments(HELLO_WORLD_RESULT, (OptionalUnaryOperator<String>) str -> null),
            arguments(FIXED_EXCEPTION_OPTIONAL_RESULT, null)
        );
    }

    /**
     * If you have a box (monad) and a chain of functions that operates on it as the previous two did, then it
     * should not matter how you nest the flatMappings of those functions.
     *
     * @see <a href=https://miklos-martin.github.io/learn/fp/2016/03/10/monad-laws-for-regular-developers.html/>
     */
    @Test
    void testMonadLawAssociativity() {
        //given
        Function<String, Result<String>> toLowerCase = str -> Result.of((Supplier<String>) str::toLowerCase);

        //then
        assertEquals(
            HELLO_WORLD_RESULT.flatMap(FLAT_MAP_TO_UPPER_CASE).flatMap(toLowerCase),
            HELLO_WORLD_RESULT.flatMap(
                str -> FLAT_MAP_TO_UPPER_CASE.apply(str).flatMap(toLowerCase)
            )
        );
    }

    /**
     * If you have a box (monad) with a value in it and a function that takes the same type of value and returns
     * the same type of box, then flatMapping it on the box or just simply applying it to the value should yield
     * the same result.
     *
     * @see <a href=https://miklos-martin.github.io/learn/fp/2016/03/10/monad-laws-for-regular-developers.html/>
     */
    @Test
    void testMonadLawLeftIdentity() {
        //then
        assertEquals(
            HELLO_WORLD_RESULT.flatMap(FLAT_MAP_TO_UPPER_CASE), FLAT_MAP_TO_UPPER_CASE.apply("Hello world")
        );
    }

    /**
     * If you have a box (monad) with a value in it and you have a function that takes the same type of value and
     * wraps it in the same kind of box untouched, then after flatMapping that function on your box should not
     * change it.
     *
     * @see <a href=https://miklos-martin.github.io/learn/fp/2016/03/10/monad-laws-for-regular-developers.html/>
     */
    @Test
    void testMonadLawRightIdentity() {
        //when
        Function<String, Result<String>> wrapInResult = str -> Result.of(() -> str);

        //then
        assertEquals(
            HELLO_WORLD_RESULT, HELLO_WORLD_RESULT.flatMap(wrapInResult)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOf")
    void testOf(Result<String> expected, boolean isEqual) {

        //then
        assertEquals(isEqual, expected.equals(HELLO_WORLD_RESULT));
    }

    @ParameterizedTest
    @MethodSource("provideOf")
    void testOfOptional(Result<String> expected, boolean isEqual) {

        //then
        assertEquals(isEqual, expected.equals(HELLO_WORLD_OPTIONAL_RESULT));
    }

    private static Stream<Arguments> provideOf() {
        return Stream.of(
            arguments(HELLO_WORLD_RESULT, true),
            arguments(HELLO_WORLD_OPTIONAL_RESULT, true),
            arguments(Result.of(() -> "Other"), false),
            arguments(Result.of(() -> Optional.of("Other")), false),
            arguments(EMPTY_RESULT, false),
            arguments(SUPPLY_UNIQUE_EXCEPTION_RESULT.get(), false),
            arguments(FIXED_EXCEPTION_OPTIONAL_RESULT, false)
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