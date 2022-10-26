package eu.borglum.functional.core;

import java.util.Optional;

@FunctionalInterface
public interface OptionalSupplier<T> {

    Optional<T> get();
}
