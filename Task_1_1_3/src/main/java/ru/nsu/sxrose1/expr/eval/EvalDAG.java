package ru.nsu.sxrose1.expr.eval;

import java.util.Optional;

public abstract class EvalDAG {
  private boolean evaluated = false;
  private Optional<Double> evaluatedValue = Optional.empty();

  protected abstract Optional<Double> evalImpl(EvalContext ctx);

  public final Optional<Double> eval(EvalContext ctx) {
    if (!evaluated) {
      evaluatedValue = evalImpl(ctx);
      evaluated = true;
    }

    return evaluatedValue;
  }
}
