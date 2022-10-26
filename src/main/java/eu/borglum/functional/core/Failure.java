package eu.borglum.functional.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Failure<T> implements Result<T> {

    private final Exception exception;

    Failure(Exception exception) {
        this.exception = exception;
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
