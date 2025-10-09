package ru.nsu.sxrose1.expr;

import java.util.function.BinaryOperator;

public class Mul extends AssociativeBinaryExpression {

  /**
   * @param lhs Left hand side of expression.
   * @param rhs Right hand side of expression.
   */
  public Mul(Expression lhs, Expression rhs) {
    super(lhs, rhs);
  }

  /** {@inheritDoc} */
  @Override
  public BinaryOperator<Double> operator() {
    return (a, b) -> a * b;
  }

  /** {@inheritDoc} */
  @Override
  public double identity() {
    return 1.0;
  }

  /** {@inheritDoc} */
  @Override
  protected String repr() {
    return "*";
  }
}
