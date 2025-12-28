package ru.nsu.sxrose1.exprtests;

import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.sxrose1.expr.Number;
import ru.nsu.sxrose1.expr.eval.EvalUtils;
import ru.nsu.sxrose1.expr.parse.ParseUtils;

/** Tests for parsing. */
public class ParsingTest {
    @Test
    void parseTest() {
        var single = ParseUtils.parse("42.123");

        Assertions.assertTrue(single.isPresent());
        Assertions.assertInstanceOf(ru.nsu.sxrose1.expr.Number.class, single.get());

        Assertions.assertEquals(42.123d, ((Number) single.get()).value);

        var negSingle = ParseUtils.parse("-666.6");
        Assertions.assertTrue(negSingle.isPresent());

        Assertions.assertEquals(Optional.of(-666.6d), EvalUtils.eval(negSingle.get(), Map.of()));

        var e = ParseUtils.parse("(x + -(0 + x - x)) / -(x * x) + (42 + 22) - 64");
        Assertions.assertTrue(e.isPresent());

        var reparsed = ParseUtils.parse(e.get().toString());
        Assertions.assertTrue(reparsed.isPresent());

        Assertions.assertEquals(
                EvalUtils.eval(e.get(), Map.of("x", 2.0d)),
                EvalUtils.eval(reparsed.get(), Map.of("x", 2.0d)));
    }
}
