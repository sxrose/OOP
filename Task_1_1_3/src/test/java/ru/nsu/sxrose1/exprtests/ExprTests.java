package ru.nsu.sxrose1.exprtests;

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
import ru.nsu.sxrose1.expr.eval.EvalDAG;
import ru.nsu.sxrose1.expr.eval.EvalUtils;
import ru.nsu.sxrose1.expr.parse.ParseUtils;

import java.util.Map;
import java.util.Optional;

public class ExprTests {
  @Test
  void constructionTest() {
    // (x^2 + 42 * y - c) / (1 + 2 + 3)
    Expression e =
        new Div( // 1
            new Sub( // 2
                new Add( // 3
                    // 4
                    new Mul(new Variable("x"), new Variable("x")), // 5
                    new Mul(new Number(42.0), new Variable("y"))),
                new Variable("c")),
            new Add(new Add(new Number(1.0), new Number(2.0)), new Number(3.0)));

    Assertions.assertEquals(5, e.height());

    Expression single = new Number(4.2);
    Assertions.assertEquals(1, single.height());

    // x - x + x - x + x - x + x
    Expression lng =
        new Add( // 1
            new Sub( // 2
                new Add( // 3
                    new Sub( // 4
                        // 5
                        /* 6 */ new Add(
                            new Sub(/* 7 */ new Variable("x"), new Variable("x")),
                            new Variable("x")),
                        new Variable("x")),
                    new Variable("x")),
                new Variable("x")),
            new Variable("x"));

    Assertions.assertEquals(7, lng.height());
  }

  @Test
  void simplifyTest() {
    // (x ^ 3 + lmao) * (zzz + 1 + 0.111) * (2 * 2)
    Expression leBig =
        new Mul(
            new Mul(
                new Add(
                    new Mul(new Mul(new Variable("x"), new Variable("x")), new Variable("x")),
                    new Variable("lmao")),
                new Add(new Add(new Variable("zzz"), new Number(1.0)), new Number(0.111))),
            new Number(4.0));

    Assertions.assertEquals(leBig, new Mul(leBig, new Number(1.0)).simplify());
    Assertions.assertEquals(leBig, new Mul(new Number(1.0), leBig).simplify());
    Assertions.assertEquals(new Number(0.0), new Mul(leBig, new Number(0.0)).simplify());
    Assertions.assertEquals(new Number(0.0), new Mul(new Number(0.0), leBig).simplify());

    Assertions.assertEquals(leBig, new Add(leBig, new Number(0.0)).simplify());
    Assertions.assertEquals(leBig, new Add(new Number(0.0), leBig).simplify());

    Assertions.assertEquals(leBig, new Div(leBig, new Number(1.0)).simplify());
    Assertions.assertEquals(new Number(0.0), new Div(new Number(0.0), leBig).simplify());

    Assertions.assertEquals(new Number(0.0), new Sub(leBig.clone(), leBig.clone()).simplify());
  }

  @Test
  void evalTest() {
    Assertions.assertEquals(Optional.of(4.2d), EvalUtils.eval(new Number(4.2), Map.of()));
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

    // (x + (0 + x - x)) / x ^ 2 + (42 + 22) - 64
    // -> x / x^2
    Expression e2 =
        new Sub(
            new Add(
                new Div(
                    new Add(
                        new Variable("x"),
                        new Sub(new Add(new Number(0.0), new Variable("x")), new Variable("x"))),
                    new Mul(new Variable("x"), new Variable("x"))),
                new Add(new Number(42.0), new Number(22.0))),
            new Number(64.0));

    EvalDAG dag = EvalUtils.compileDAG(e2, EvalUtils.COMPILE_OPT_SIMPLIFY);

    Assertions.assertEquals(Optional.of(0.5d), dag.eval(new EvalContext(Map.of("x", 2.0d))));
  }

  @Test
  void derivativeTest() {
    Assertions.assertEquals(new Number(1.0), new Variable("x").derivative("x"));
    Assertions.assertEquals(new Number(0.0), new Variable("x").derivative("y"));

    Assertions.assertEquals(new Number(0.0), new Number(42.0).derivative("lol"));
  }

  @Test
  void parseTest() {
    var single = ParseUtils.parse("42.123");

    Assertions.assertTrue(single.isPresent());
    Assertions.assertInstanceOf(Number.class, single.get());

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
