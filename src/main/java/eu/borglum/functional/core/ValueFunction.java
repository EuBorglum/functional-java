package eu.borglum.functional.core;

import java.util.function.Function;

@FunctionalInterface
public interface ValueFunction<T, R> extends Function<T, R> {
}
