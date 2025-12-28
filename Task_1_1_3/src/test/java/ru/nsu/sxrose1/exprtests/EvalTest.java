package ru.nsu.sxrose1.exprtests;

import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.sxrose1.expr.Add;
import ru.nsu.sxrose1.expr.Div;
import ru.nsu.sxrose1.expr.Expression;
import ru.nsu.sxrose1.expr.Mul;
import ru.nsu.sxrose1.expr.Number;
import ru.nsu.sxrose1.expr.Sub;
import ru.nsu.sxrose1.expr.Variable;
import ru.nsu.sxrose1.expr.eval.EvalContext;
import ru.nsu.sxrose1.expr.eval.EvalUtils;
import ru.nsu.sxrose1.expr.eval.EvaluationGraph;

/** Tests for evaluation classes. */
public class EvalTest {
    @Test
    void basicEvalTest() {
        Assertions.assertEquals(
                Optional.of(4.2d), EvalUtils.eval(new ru.nsu.sxrose1.expr.Number(4.2), Map.of()));
        Assertions.assertEquals(
                Optional.of(6.9d), EvalUtils.eval(new Variable("xyz"), Map.of("xyz", 6.9d)));

        // x * y + z/2 - c
        Expression e =
                new Sub(
                        new Add(
                                new Mul(new Variable("x"), new Variable("y")),
                                new Div(new Variable("z"), new Number(2.0))),
                        new Variable("c"));

        Assertions.assertEquals(
                Optional.of(42.0d * 2.213d + 0.5d - 13.37d),
                EvalUtils.eval(e, Map.of("x", 42.0, "y", 2.213, "z", 1.0, "c", 13.37)));

        Assertions.assertEquals(
                Optional.empty(), EvalUtils.eval(e, Map.of("x", 42.0, "y", 2.213, "z", 1.0)));
    }

    @Test
    void compilationTest() {
        // (x + (0 + x - x)) / x ^ 2 + (42 + 22) - 64
        // -> x / x^2
        Expression e2 =
                new Sub(
                        new Add(
                                new Div(
                                        new Add(
                                                new Variable("x"),
                                                new Sub(
                                                        new Add(new Number(0.0), new Variable("x")),
                                                        new Variable("x"))),
                                        new Mul(new Variable("x"), new Variable("x"))),
                                new Add(new Number(42.0), new Number(22.0))),
                        new Number(64.0));

        EvaluationGraph dag = EvalUtils.compileEvalGraph(e2, EvalUtils.COMPILE_OPT_SIMPLIFY);

        Assertions.assertEquals(Optional.of(0.5d), dag.eval(new EvalContext(Map.of("x", 2.0d))));
    }

    @Test
    void invalidResultTest() {
        Assertions.assertEquals(
                Optional.empty(),
                EvalUtils.eval(new Div(new Number(0), new Number(0)), new EvalContext(Map.of())));

        Assertions.assertEquals(
                Optional.empty(),
                EvalUtils.eval(
                        new Div(new Variable("x"), new Number(0)),
                        new EvalContext(Map.of("x", 0.0))));

        Assertions.assertEquals(
                Optional.empty(),
                EvalUtils.eval(
                        new Add(new Variable("x"), new Variable("x")),
                        new EvalContext(Map.of("x", Double.NaN))));
    }
}
