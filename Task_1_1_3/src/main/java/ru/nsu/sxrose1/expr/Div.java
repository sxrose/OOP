package ru.nsu.sxrose1.expr;

import java.util.function.BinaryOperator;

public class Div extends BinaryExpression {
  /**
   * @param lhs Left hand side of expression.
   * @param rhs Right hand side of expression.
   */
  public Div(Expression lhs, Expression rhs) {
    super(lhs, rhs);
  }

  /** {@inheritDoc} */
  @Override
  public BinaryOperator<Double> operator() {
    return (a, b) -> a / b;
  }

  /** {@inheritDoc} */
  @Override
  protected Expression simplifyCancelOutInplace() {
    if (lhs instanceof Number) {
      if (((Number) lhs).value == 0.0) return new Number(0.0);
    }

    if (rhs instanceof Number) {
      if (((Number) rhs).value == 1.0) return lhs;
    }

    return this;
  }

  /** {@inheritDoc} */
  @Override
  protected String repr() {
    return "/";
  }
}
