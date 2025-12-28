package ru.nsu.sxrose1.expr.eval;

import java.util.Optional;
import java.util.function.BinaryOperator;

/** Represents EvalDAG with two edges. */
public class BiEvalDaG extends EvalDaG {

    public EvalDaG left;
    public EvalDaG right;
    public BinaryOperator<Double> op;

    BiEvalDaG(EvalDaG left, EvalDaG right, BinaryOperator<Double> op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    protected Optional<Double> evalImpl(EvalContext ctx) {
        return left.eval(ctx).flatMap((l) -> right.eval(ctx).map((r) -> op.apply(l, r)));
    }
}
