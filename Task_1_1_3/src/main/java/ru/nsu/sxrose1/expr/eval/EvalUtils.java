package ru.nsu.sxrose1.expr.eval;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import ru.nsu.sxrose1.expr.BinaryExpression;
import ru.nsu.sxrose1.expr.Expression;
import ru.nsu.sxrose1.expr.Number;
import ru.nsu.sxrose1.expr.Variable;

/** Util class for evaluation. */
public class EvalUtils {

    /** Flag for compileDAG method, simplifies expression before constructing DAG if set. */
    public static final int COMPILE_OPT_SIMPLIFY = 0b1;

    private static EvaluationGraph compileDAGImpl(Expression e, HashMap<Expression, EvaluationGraph> acc) {
        EvaluationGraph dag = acc.get(e);
        if (Objects.nonNull(dag)) {
            return dag;
        }

        return switch (e) {
            case Number n -> {
                dag = new ImmEvaluationGraph(n);
                acc.put(e, dag);
                yield dag;
            }
            case Variable v -> {
                dag = new ImmEvaluationGraph(v);
                acc.put(e, dag);
                yield dag;
            }
            case BinaryExpression be -> {
                EvaluationGraph l = compileDAGImpl(be.lhs, acc);
                EvaluationGraph r = compileDAGImpl(be.rhs, acc);

                yield new BiEvaluationGraph(l, r, be.operator());
            }
            default -> throw new AssertionError("Unhandled case");
        };
    }

    /**
     * Compiles Expression to EvalDAG.
     *
     * @param e Expression to compile.
     * @param compileOpts compilation options, see EvalUtils.COMPILE_OPT_*.
     * @return compiled EvalDAG.
     */
    public static EvaluationGraph compileDAG(Expression e, int compileOpts) {
        if ((compileOpts & COMPILE_OPT_SIMPLIFY) != 0) {
            e = e.simplify();
        }

        return compileDAGImpl(e, new HashMap<>());
    }

    /**
     * Evaluates Expression.
     *
     * @param e Expression to evaluate.
     * @param mapping mappings of variables.
     * @return value with respect to mappings if mappings are complete, empty otherwise.
     */
    public static Optional<Double> eval(Expression e, Map<String, Double> mapping) {
        EvaluationGraph dag = compileDAG(e, 0);
        return dag.eval(new EvalContext(mapping));
    }
}
