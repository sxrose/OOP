package ru.nsu.sxrose1.expr;

import java.util.function.BinaryOperator;

public final class Add extends AssociativeBinaryExpression {
  /**
   * @param lhs Left hand side of expression.
   * @param rhs Right hand side of expression.
   */
  public Add(Expression lhs, Expression rhs) {
    super(lhs, rhs);
  }

  /** {@inheritDoc} */
  @Override
  public BinaryOperator<Double> operator() {
    return Double::sum;
  }

  /** {@inheritDoc} */
  @Override
  public double identity() {
    return 0.0;
  }

  /** {@inheritDoc} */
  @Override
  protected String repr() {
    return "+";
  }
}
