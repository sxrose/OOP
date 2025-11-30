package ru.nsu.sxrose1.studentbook.data;

import ru.nsu.sxrose1.extra.OptionalUtils;

import java.util.ArrayList;
import java.util.Optional;

public record AssessmentInfo(ArrayList<Grade> grades) {
    public int getAmountOf() {
        return grades.size();
    }

    public Optional<Double> getMean() {
        if (grades.isEmpty()) {
            return Optional.empty();
        }
        var total =
                grades.stream()
                        .map(Grade::getValue)
                        .reduce(Optional.of(0), OptionalUtils.liftOperator(Integer::sum));

        return total.flatMap((t) -> Optional.of(t.doubleValue() / grades.size()));
    }
}
