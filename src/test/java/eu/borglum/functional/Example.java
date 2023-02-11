package eu.borglum.functional;

import eu.borglum.functional.core.OptionalSupplier;
import eu.borglum.functional.core.Result;

import java.util.Optional;
import java.util.function.Supplier;

public class Example {

    public static void main(String[] args) {

        Result.of(() -> "Hello world");

        Supplier<String> strSup = () -> "Hello world";
        Result.ofValue(strSup);

        Result.ofOptional(() -> Optional.of("Hello world"));

        Supplier<Optional<String>> strOptSup = () -> Optional.of("Hello world");
        Result.ofOptional(strOptSup);


        Result.of((OptionalSupplier<Object>) Optional::empty);

        System.out.println(
        Result
            .of(() -> "Value")
            .mapOptional(Optional::of)
        );

        OptionalSupplier<String> strOpt = () -> Optional.of("Value");
        Result.of(strOpt);
    }
}
