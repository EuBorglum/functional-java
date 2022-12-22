package eu.borglum.functional.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Success<T> implements InternalResult<T>, Result<T> {

    private final Optional<T> optionalValue;

    private Success(Optional<T> optionalValue) {

        this.optionalValue = optionalValue;
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
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Success<?> success = (Success<?>) o;

        return new EqualsBuilder()
            .append(optionalValue, success.optionalValue)
            .isEquals();
    }

    @Override
    public Result<T> filter(Predicate<? super T> predicate) {

        Objects.requireNonNull(predicate);

        return Result.of(
            () -> optionalValue.filter(predicate)
        );
    }

    @Override
    public <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> function) {

        Objects.requireNonNull(function);

        if (optionalValue.isPresent()) {
            Result<U> newResult;
            try {
                //noinspection unchecked
                newResult = (Result<U>) function.apply(optionalValue.get());
            } catch (Exception e) {
                return Failure.create(e);
            }

            return Objects.requireNonNull(newResult);

        } else {
            return Success.create();
        }
    }

    @Override
    public boolean isFailure() {

        return false;
    }

    @Override
    public boolean isSuccess() {

        return true;
    }

    @Override
    public Exception getCause() {

        throw new UnsupportedOperationException("A success does not contain a cause of failure");
    }

    @Override
    public Optional<T> getOptional() {

        return optionalValue;
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37)
            .append(optionalValue)
            .toHashCode();
    }

    @Override
    public <U> Result<U> map(Function<? super T, ? extends U> function) {

        return mapValue(function);
    }

    @Override
    public <U> Result<U> map(OptionalFunction<? super T, ? extends U> function) {

        return mapOptional(function);
    }

    @Override
    public <X extends Exception> Result<T> mapFailure(Class<X> exceptionClass,
                                                      Function<? super X, ? extends Exception> function) {
        validate(exceptionClass, function);

        return create(optionalValue);
    }

    @Override
    public <U> Result<U> mapOptional(OptionalFunction<? super T, ? extends U> function) {

        Objects.requireNonNull(function);

        //noinspection unchecked
        return (Result<U>) optionalValue
            .map(v -> Result.of(
                () -> function.apply(v)
            ))
            .orElseGet(Success::create);
    }

    @Override
    public <U> Result<U> mapValue(Function<? super T, ? extends U> function) {

        Objects.requireNonNull(function);

        //noinspection unchecked
        return (Result<U>) optionalValue
            .map(v -> Result.of(
                () -> function.apply(v)
            ))
            .orElseGet(Success::create);
    }

    @Override
    public T orElseGet(Supplier<Switch<Exception, T>> supplier) {

        Objects.requireNonNull(supplier);

        return InternalResult
            .of(this)
            .getOptional()
            .orElse(null);
    }

    @Override
    public <X extends Exception> Result<T> recover(Class<X> exceptionClass, Function<? super X, ? extends T> function) {

        return recoverValue(exceptionClass, function);
    }

    @Override
    public <X extends Exception> Result<T> recover(Class<X> exceptionClass,
                                                   OptionalFunction<? super X, ? extends T> function) {

        return recoverOptional(exceptionClass, function);
    }

    @Override
    public <X extends Exception> Result<T> recoverOptional(Class<X> exceptionClass,
                                                           OptionalFunction<? super X, ? extends T> function) {
        validate(exceptionClass, function);

        return create(optionalValue);
    }

    @Override
    public <X extends Exception> Result<T> recoverValue(Class<X> exceptionClass,
                                                        Function<? super X, ? extends T> function) {
        validate(exceptionClass, function);

        return create(optionalValue);
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("optionalValue", optionalValue)
            .toString();
    }

    private <X extends Exception> void validate(Class<X> exceptionClass, Function<?, ?> function) {

        Objects.requireNonNull(exceptionClass);

        Objects.requireNonNull(function);
    }
}
