package eu.borglum.functional.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static eu.borglum.functional.TestDataFactory.create;
import static eu.borglum.functional.TestDataFactory.flatMapOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ResultTest {

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    /**
     * If you have a monad and a chain of functions that operates on it, then it should not matter how you nest
     * the flatMappings of those functions.
     *
     * @see <a href=https://miklos-martin.github.io/learn/fp/2016/03/10/monad-laws-for-regular-developers.html/>
     */
    @Test
    void testMonadLawAssociativity() {

        //given
        Result<String> value = create("Value");

        Function<? super String, ? extends Result<? extends String>> toLowerCase = str
            -> Result.ofValue(str::toLowerCase);

        Function<? super String, ? extends Result<? extends String>> toUpperCase = str
            -> Result.ofValue(str::toUpperCase);

        //then
        assertEquals(
            value.flatMap(toUpperCase).flatMap(toLowerCase),
            value.flatMap(str -> toUpperCase.apply(str).flatMap(toLowerCase))
        );
    }

    /**
     * If you have a monad with a value in it and a function that takes the same type of value and returns
     * the same type of box, then flatMapping it on the box or just simply applying it to the value should yield
     * the same result.
     *
     * @see <a href=https://miklos-martin.github.io/learn/fp/2016/03/10/monad-laws-for-regular-developers.html/>
     */
    @Test
    void testMonadLawLeftIdentity() {

        //given
        Result<String> value = create("Value");

        Function<? super String, ? extends Result<? extends String>> toUpperCase = str
            -> Result.ofValue(str::toUpperCase);

        //then
        assertEquals(value.flatMap(toUpperCase), toUpperCase.apply("Value"));
    }

    /**
     * If you have a monad with a value in it and you have a function that takes the same type of value and
     * wraps it in the same kind of box untouched, then after flatMapping that function on your box should not
     * change it.
     *
     * @see <a href=https://miklos-martin.github.io/learn/fp/2016/03/10/monad-laws-for-regular-developers.html/>
     */
    @Test
    void testMonadLawRightIdentity() {

        //given
        Result<String> value = create("Value");

        //then
        assertEquals(
            value, value.flatMap(flatMapOf())
        );
    }

    @ParameterizedTest
    @MethodSource("provideOf")
    void testOf(Result<String> actual, Result<String> expected) {

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideOf() {
        Result<String> empty = create();
        Result<String> illegalState = create(ILLEGAL_STATE_EXCEPTION);
        Result<String> optional = create(Optional.of("Value"));
        Result<String> value = create("Value");

        return Stream.of(
            arguments(empty, empty),
            arguments(empty, create()),
            arguments(illegalState, illegalState),
            arguments(illegalState, create(ILLEGAL_STATE_EXCEPTION)),
            arguments(optional, optional),
            arguments(optional, create(Optional.of("Value"))),
            arguments(value, value),
            arguments(value, create("Value")),
            arguments(value, optional)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOfInvalid")
    void testOfInvalid(Supplier<Result<String>> invalid) {

        //then
        assertThrows(NullPointerException.class, invalid::get);
    }

    private static Stream<Arguments> provideOfInvalid() {
        return Stream.of(
            arguments((Supplier<Result<String>>) () -> Result.ofOptional(null)),
            arguments((Supplier<Result<String>>) () -> Result.ofOptional(() -> null)),
            arguments((Supplier<Result<String>>) () -> Result.ofValue(null)),
            arguments((Supplier<Result<String>>) () -> Result.ofValue(() -> null))
        );
    }
}