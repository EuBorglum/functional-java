package eu.borglum.functional.core;

import java.util.Optional;
import java.util.function.Supplier;

@FunctionalInterface
public interface OptionalSupplier<T> extends Supplier<Optional<T>> {
}
