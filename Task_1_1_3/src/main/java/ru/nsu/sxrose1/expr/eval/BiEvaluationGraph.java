package ru.nsu.sxrose1.expr.eval;

import java.util.Optional;
import java.util.function.BinaryOperator;

/** Represents EvalDAG with two edges. */
public class BiEvaluationGraph extends EvaluationGraph {

    public EvaluationGraph left;
    public EvaluationGraph right;
    public BinaryOperator<Double> op;

    BiEvaluationGraph(EvaluationGraph left, EvaluationGraph right, BinaryOperator<Double> op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    protected Optional<Double> evalImpl(EvalContext ctx) {
        return left.eval(ctx).flatMap((l) -> right.eval(ctx).map((r) -> op.apply(l, r)));
    }
}
