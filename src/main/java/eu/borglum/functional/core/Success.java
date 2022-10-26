package eu.borglum.functional.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Optional;

public class Success<T> implements Result<T> {

    private final Optional<T> value;

    Success(Optional<T> value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Success<?> success = (Success<?>) o;

        return new EqualsBuilder()
            .append(value, success.value)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(value)
            .toHashCode();
    }
}
