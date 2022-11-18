package eu.borglum.functional.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

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
    public Result<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);

        return Result.of(
            () -> value.filter(predicate)
        );
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

        //noinspection unchecked
        return (Result<U>) value
            .map(v -> Result.of(
                () -> function.apply(v)
            ))
            .orElseGet(Success::create);
    }

    @Override
    public <U> Result<U> map(OptionalFunction<? super T, ? extends U> function) {
        Objects.requireNonNull(function);

        //noinspection unchecked
        return (Result<U>) value
            .map(v -> Result.of(
                () -> function.apply(v)
            ))
            .orElseGet(Success::create);
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

    @Override
    public <X extends Exception> Result<T> recover(Class<X> exceptionClass, Function<? super X, ? extends T> function) {
        Objects.requireNonNull(exceptionClass);
        Objects.requireNonNull(function);

        return create(value);
    }

    @Override
    public <X extends Exception> Result<T> recover(Class<X> exceptionClass, OptionalFunction<? super X, ? extends T> function) {
        Objects.requireNonNull(exceptionClass);
        Objects.requireNonNull(function);

        return create(value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("value", value)
            .toString();
    }
}
