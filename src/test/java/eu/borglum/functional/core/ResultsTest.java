package eu.borglum.functional.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.borglum.functional.core.TestDataFactory.create;
import static eu.borglum.functional.core.TestDataFactory.createList;
import static eu.borglum.functional.core.TestDataFactory.createListOptional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ResultsTest {

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    @ParameterizedTest
    @MethodSource("provideSequence")
    void testSequenceList(List<Result<String>> initial, Result<?> expected) {

        //when
        Result<List<String>> actual = Results.sequence(initial);

        //then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideSequence")
    void testSequenceStream(List<Result<String>> initial, Result<?> expected) {

        //when
        Result<Stream<String>> actual = Results.sequence(initial.stream());

        //then
        assertEquals(expected, actual.mapValue(stream -> stream.collect(Collectors.toList())));
    }

    private static Stream<Arguments> provideSequence() {
        return Stream.of(
            arguments(createList(values()), create(values())),
            arguments(createListOptional(valuesWithNull()), create(valuesWithNull())),
            arguments(createListOptional(Collections.emptyList()), create(Collections.emptyList())),
            arguments(createList(values(), ILLEGAL_STATE_EXCEPTION), create(ILLEGAL_STATE_EXCEPTION)),
            arguments(createList(ILLEGAL_STATE_EXCEPTION, values()), create(ILLEGAL_STATE_EXCEPTION))
        );
    }

    private static List<String> values() {
        return Arrays.asList("value1", "value2");
    }

    private static List<String> valuesWithNull() {
        return Arrays.asList("value1", null, "value3");
    }
}