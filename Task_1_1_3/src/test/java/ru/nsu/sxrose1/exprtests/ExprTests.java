package ru.nsu.sxrose1.exprtests;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.sxrose1.expr.Add;
import ru.nsu.sxrose1.expr.Div;
import ru.nsu.sxrose1.expr.Expression;
import ru.nsu.sxrose1.expr.Mul;
import ru.nsu.sxrose1.expr.Number;
import ru.nsu.sxrose1.expr.Sub;
import ru.nsu.sxrose1.expr.Variable;
import ru.nsu.sxrose1.expr.eval.EvalUtils;

/** Tests for expression classes. */
public class ExprTests {
    private void equality(Expression e) {
        Expression shallow = e.shallowCopy();
        Assertions.assertEquals(e, shallow);
        Assertions.assertTrue(e.exprEquals(shallow));

        Expression deep = e.clone();

        Assertions.assertEquals(e, deep);
        Assertions.assertTrue(e.exprEquals(deep));

        Expression e1 = new Add(new Number(0), e);

        Assertions.assertNotEquals(e, e1);
        Assertions.assertFalse(e.exprEquals(e1));
    }

    @Test
    void equalityTest() {
        equality(new Number(0));
        equality(new Variable("xyz"));
        equality(new Div(new Number(0), new Number(0)));
        equality(
                new Div(
                        new Sub(
                                new Add(
                                        new Mul(new Variable("x"), new Variable("x")),
                                        new Mul(new Number(42.0), new Variable("y"))),
                                new Variable("c")),
                        new Add(new Add(new Number(1.0), new Number(2.0)), new Number(3.0))));
    }

    private void heightPlusOne(Expression e) {
        Assertions.assertEquals(e.height() + 1, new Add(new Number(0), e).height());
    }

    @Test
    void heightTest() {
        Assertions.assertEquals(1, new Number(0).height());
        Assertions.assertEquals(1, new Variable("abc").height());

        // (x^2 + 42 * y - c) / (1 + 2 + 3)
        Expression e =
                /*1*/ new Div(
                        /*2*/ new Sub(
                                /*3*/ new Add(
                                        /*4*/ new Mul(new Variable("x"), /* 5 */ new Variable("x")),
                                        new Mul(new Number(42.0), new Variable("y"))),
                                new Variable("c")),
                        new Add(new Add(new Number(1.0), new Number(2.0)), new Number(3.0)));

        Assertions.assertEquals(5, e.height());

        heightPlusOne(new Number(0));
        heightPlusOne(new Div(new Variable("x"), new Number(69)));
        heightPlusOne(e);
    }

    @Test
    void simplifyTest() {
        // (x ^ 3 + lmao) * (zzz + 1 + 0.111) * (2 * 2)
        Expression leBig =
                new Mul(
                        new Mul(
                                new Add(
                                        new Mul(
                                                new Mul(new Variable("x"), new Variable("x")),
                                                new Variable("x")),
                                        new Variable("lmao")),
                                new Add(
                                        new Add(new Variable("zzz"), new Number(1.0)),
                                        new Number(0.111))),
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
    void basicDerivativeTest() {
        Assertions.assertEquals(new Number(1.0), new Variable("x").derivative("x"));
        Assertions.assertEquals(new Number(0.0), new Variable("x").derivative("y"));

        Assertions.assertEquals(new Number(0.0), new Number(42.0).derivative("lol"));
    }

    @Test
    void assumingEvalDerivativeTest() {
        Assertions.assertEquals(
                EvalUtils.eval(
                        new Add(new Mul(new Number(20.0), new Variable("x")), new Number(1.0)),
                        Map.of("x", 3.0d)),
                EvalUtils.eval(
                        new Add(
                                        new Sub(
                                                new Div(
                                                        new Mul(
                                                                new Mul(
                                                                        new Mul(
                                                                                new Variable("x"),
                                                                                new Variable("x")),
                                                                        new Number(12.0)),
                                                                new Variable("x")),
                                                        new Variable("x")),
                                                new Mul(
                                                        new Mul(new Number(2.0), new Variable("x")),
                                                        new Variable("x"))),
                                        new Variable("x"))
                                .derivative("x")
                                .simplify(),
                        Map.of("x", 3.0d)));
    }
}
