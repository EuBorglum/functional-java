package eu.borglum.functional.core;

import java.util.Objects;
import java.util.Optional;

public interface InternalResult<T> {

    /**
     * @return
     * @since 1.0
     */
    boolean isFailure();

    /**
     * @return
     * @since 1.0
     */
    boolean isSuccess();

    /**
     * @return
     * @since 1.0
     */
    Exception getCause();

    /**
     * @return
     * @since 1.0
     */
    Optional<T> getValue();

    /**
     * @param result
     * @param <U>
     * @return
     * @since 1.0
     */
    static <U> InternalResult<U> of(Result<U> result) {
        Objects.requireNonNull(result);

        //noinspection unchecked
        return (InternalResult<U>) result;
    }
}
