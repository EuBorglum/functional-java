package eu.borglum.functional.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.borglum.functional.TestDataFactory.create;
import static eu.borglum.functional.TestDataFactory.createList;
import static eu.borglum.functional.TestDataFactory.createListOptional;
import static eu.borglum.functional.TestDataFactory.createSet;
import static eu.borglum.functional.TestDataFactory.createSetOptional;
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
            arguments(createList(values()), create(new ArrayList<>(values()))),
            arguments(createListOptional(valuesWithNull()), create(new ArrayList<>(valuesWithNull()))),
            arguments(createListOptional(Collections.emptyList()), create(Collections.emptyList())),
            arguments(createList(values(), ILLEGAL_STATE_EXCEPTION), create(ILLEGAL_STATE_EXCEPTION)),
            arguments(createList(ILLEGAL_STATE_EXCEPTION, values()), create(ILLEGAL_STATE_EXCEPTION))
        );
    }

    @ParameterizedTest
    @MethodSource("provideSequenceSet")
    void testSequenceSet(Set<Result<String>> initial, Result<?> expected) {

        //when
        Result<Set<String>> actual = Results.sequence(initial);

        //then
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideSequenceSet() {
        return Stream.of(
            arguments(createSet(values()), create(new HashSet<>(values()))),
            arguments(createSetOptional(valuesWithNull()), create(new HashSet<>(valuesWithNull()))),
            arguments(createSetOptional(Collections.emptySet()), create(Collections.emptySet())),
            arguments(createSet(values(), ILLEGAL_STATE_EXCEPTION), create(ILLEGAL_STATE_EXCEPTION)),
            arguments(createSet(ILLEGAL_STATE_EXCEPTION, values()), create(ILLEGAL_STATE_EXCEPTION))
        );
    }

    private static Collection<String> values() {
        return Arrays.asList("value1", "value2");
    }

    private static Collection<String> valuesWithNull() {
        return Arrays.asList("value1", null, "value3");
    }
}