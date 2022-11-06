package eu.borglum.functional.core;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface OptionalFunction<T, R> extends Function<T, Optional<R>> {
}
