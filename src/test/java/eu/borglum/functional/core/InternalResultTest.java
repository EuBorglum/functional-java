package eu.borglum.functional.core;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static eu.borglum.functional.core.TestDataFactory.create;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InternalResultTest {

    private static final IllegalStateException ILLEGAL_STATE_EXCEPTION = new IllegalStateException("");

    @Test
    void testGetCauseWhenFailure() {
        //when
        InternalResult<String> result = InternalResult.of(create(ILLEGAL_STATE_EXCEPTION));

        //then
        assertEquals(ILLEGAL_STATE_EXCEPTION, result.getCause());
    }

    @Test
    void testGetCauseWhenSuccess() {
        //when
        InternalResult<String> result = InternalResult.of(create("value"));

        //then
        assertThrows(UnsupportedOperationException.class, result::getCause);
    }

    @Test
    void testGetValueWhenFailure() {
        //when
        InternalResult<String> result = InternalResult.of(create(ILLEGAL_STATE_EXCEPTION));

        //then
        IllegalStateException actual = assertThrows(IllegalStateException.class, result::getValue);
        assertEquals(ILLEGAL_STATE_EXCEPTION, actual);
    }

    @Test
    void testGetValueWhenSuccess() {
        //when
        InternalResult<String> result = InternalResult.of(create("value"));

        //then
        assertEquals(Optional.of("value"), result.getValue());
    }

    @Test
    void testIsFailure() {
        //when
        InternalResult<String> result = InternalResult.of(create(ILLEGAL_STATE_EXCEPTION));

        //then
        assertAll(
            () -> assertTrue(result.isFailure()),
            () -> assertFalse(result.isSuccess())
        );
    }

    @Test
    void testIsSuccess() {
        //when
        InternalResult<String> result = InternalResult.of(create());

        //then
        assertAll(
            () -> assertFalse(result.isFailure()),
            () -> assertTrue(result.isSuccess())
        );
    }
}
