package ru.nsu.sxrose1.expr.eval;

import java.util.Optional;

/** Represent EvalDAG - abstraction for efficiently evaluating expressions to values. */
public abstract class EvaluationGraph {
    private boolean evaluated = false;
    private Optional<Double> evaluatedValue = Optional.empty();

    protected abstract Optional<Double> evalImpl(EvalContext ctx);

    /**
     * Evaluate DAG with respect to context.
     *
     * @param ctx evaluation context.
     * @return evaluation result if context's mapping is complete with respect to expression that
     *     DAG constitute, empty otherwise.
     */
    public final Optional<Double> eval(EvalContext ctx) {
        if (!evaluated) {
            evaluatedValue = evalImpl(ctx);
            evaluated = true;
        }

        return evaluatedValue;
    }
}
