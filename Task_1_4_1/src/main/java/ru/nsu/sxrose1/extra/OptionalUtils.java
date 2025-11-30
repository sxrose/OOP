package ru.nsu.sxrose1.extra;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class OptionalUtils {
    public static <T> BinaryOperator<Optional<T>> liftOperator(BinaryOperator<T> op) {
        return (x, y) -> x.flatMap((v1) -> y.flatMap((v2) -> Optional.of(op.apply(v1, v2))));
    }
}
