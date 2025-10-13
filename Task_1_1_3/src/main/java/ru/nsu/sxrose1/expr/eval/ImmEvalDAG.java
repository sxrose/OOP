package ru.nsu.sxrose1.expr.eval;

import java.util.Optional;
import java.util.function.Function;
import ru.nsu.sxrose1.expr.Number;
import ru.nsu.sxrose1.expr.Variable;

/** Represents EvalDAG with immediately available value (leaf node of DAG). */
public class ImmEvalDAG extends EvalDAG {
  private final Function<EvalContext, Optional<Double>> producer;

  ImmEvalDAG(Number num) {
    producer = (_ctx) -> Optional.of(num.value);
  }

  ImmEvalDAG(Variable variable) {
    producer = (ctx) -> Optional.ofNullable(ctx.get(variable.name));
  }

  @Override
  protected Optional<Double> evalImpl(EvalContext ctx) {
    return producer.apply(ctx);
  }
}
