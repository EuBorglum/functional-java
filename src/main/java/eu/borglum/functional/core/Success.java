package eu.borglum.functional.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Success<T> implements Result<T> {

    private final Optional<T> value;

    private Success(Optional<T> value) {
        this.value = value;
    }

    static <U> Success<U> create() {
        return new Success<>(Optional.empty());
    }

    static <U> Success<U> create(U value) {
        Objects.requireNonNull(value);
        return new Success<>(Optional.of(value));
    }

    static <U> Success<U> create(Optional<U> value) {
        Objects.requireNonNull(value);
        return new Success<>(value);
    }

    @Override
    public <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> function) {
        Objects.requireNonNull(function);

        if (value.isPresent()) {
            Result<U> newResult;
            try {
                //noinspection unchecked
                newResult = (Result<U>) function.apply(value.get());
            } catch (Exception e) {
                return Failure.create(e);
            }

            return Objects.requireNonNull(newResult);

        } else {
            return Success.create();
        }
    }

    @Override
    public <U> Result<U> map(Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);

        if (value.isPresent()) {
            U newValue;
            try {
                newValue = function.apply(value.get());
            } catch (Exception e) {
                return Failure.create(e);
            }

            return Success.create(newValue);

        } else {
            return Success.create();
        }
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
