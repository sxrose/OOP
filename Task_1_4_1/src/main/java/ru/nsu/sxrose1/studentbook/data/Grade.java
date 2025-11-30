package ru.nsu.sxrose1.studentbook.data;

import java.util.Optional;

public enum Grade {
    UNGRADED,
    EXCELLENT,
    GOOD,
    PASS,
    NON_PASS;

    static Optional<Integer> getValue(Grade g) {
        return switch (g) {
            case UNGRADED -> Optional.empty();
            case EXCELLENT -> Optional.of(5);
            case GOOD -> Optional.of(4);
            case PASS -> Optional.of(3);
            case NON_PASS -> Optional.of(2);
        };
    }
}
