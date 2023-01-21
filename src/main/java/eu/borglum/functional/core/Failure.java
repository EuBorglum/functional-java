package eu.borglum.functional.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

final class Failure<T> implements InternalResult<T>, Result<T> {

    private final Exception exception;

    private Failure(Exception exception) {

        this.exception = exception;
    }

    static <U> Failure<U> create(Exception exception) {

        Objects.requireNonNull(exception);

        return new Failure<>(exception);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Failure<?> failure = (Failure<?>) o;

        return new EqualsBuilder()
            .append(exception, failure.exception)
            .isEquals();
    }

    @Override
    public Result<T> filter(Predicate<? super T> predicate) {

        Objects.requireNonNull(predicate);

        return create(exception);
    }

    @Override
    public <U> Result<U> flatMap(Function<? super T, ? extends Result<? extends U>> function) {

        Objects.requireNonNull(function);

        return create(exception);
    }

    @Override
    public boolean isFailure() {

        return true;
    }

    @Override
    public boolean isSuccess() {

        return false;
    }

    @Override
    public Exception getCause() {

        return exception;
    }

    @Override
    public Optional<T> getOptional() {

        return Optional.of(throwException());
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37)
            .append(exception)
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

        Optional<? extends InternalResult<? extends Exception>> internalResult = Optional
            .of(exception)
            .filter(ex -> exceptionClass.isAssignableFrom(ex.getClass()))
            .map(exceptionClass::cast)
            .map(ex -> Result.of(() -> function.apply(ex)))
            .map(InternalResult::of);

        //noinspection unchecked
        return (Result<T>) internalResult
            .filter(InternalResult::isSuccess)
            .flatMap(InternalResult::getOptional)
            .map(Exception.class::cast)
            .or(
                () -> internalResult
                    .filter(InternalResult::isFailure)
                    .map(InternalResult::getCause)
            )
            .map(Failure::create)
            .orElseGet(() -> create(exception));
    }

    @Override
    public <U> Result<U> mapOptional(OptionalFunction<? super T, ? extends U> function) {

        Objects.requireNonNull(function);

        return create(exception);
    }

    @Override
    public <U> Result<U> mapValue(Function<? super T, ? extends U> function) {

        Objects.requireNonNull(function);

        return create(exception);
    }

    @Override
    public T orElseRecover(SwitchSupplier<Exception, T> supplier) {

        Objects.requireNonNull(supplier);

        return supplier
            .get()
            .evaluate(exception)
            .orElseGet(this::throwException);
    }

    @Override
    public T orElseThrow() {

        return throwException();
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

        //noinspection unchecked
        return (Result<T>) Optional
            .of(exception)
            .filter(ex -> exceptionClass.isAssignableFrom(ex.getClass()))
            .map(exceptionClass::cast)
            .map(ex -> Result.of(() -> function.apply(ex)))
            .orElseGet(() -> create(exception));
    }

    @Override
    public <X extends Exception> Result<T> recoverValue(Class<X> exceptionClass,
                                                        Function<? super X, ? extends T> function) {
        validate(exceptionClass, function);

        //noinspection unchecked
        return (Result<T>) Optional
            .of(exception)
            .filter(ex -> exceptionClass.isAssignableFrom(ex.getClass()))
            .map(exceptionClass::cast)
            .map(ex -> Result.of(() -> function.apply(ex)))
            .orElseGet(() -> create(exception));
    }

    private <E extends Exception> T throwException() throws E {

        //noinspection unchecked
        throw (E) exception;
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("exception", exception)
            .toString();
    }

    private <X extends Exception> void validate(Class<X> exceptionClass, Function<?, ?> function) {

        Objects.requireNonNull(exceptionClass);

        Objects.requireNonNull(function);
    }
}
