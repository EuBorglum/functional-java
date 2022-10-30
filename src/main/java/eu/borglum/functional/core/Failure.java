package eu.borglum.functional.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;
import java.util.function.Function;

public class Failure<T> implements Result<T> {

    private final Exception exception;

    private Failure(Exception exception) {
        this.exception = exception;
    }

    static <U> Failure<U> create(Exception exception) {
        Objects.requireNonNull(exception);

        return new Failure<>(exception);
    }

    @Override
    public <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> function) {
        Objects.requireNonNull(function);

        return new Failure<>(exception);
    }

    @Override
    public <U> Result<U> map(Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);

        return new Failure<>(exception);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Failure<?> failure = (Failure<?>) o;

        return new EqualsBuilder()
            .append(exception, failure.exception)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(exception)
            .toHashCode();
    }
}
