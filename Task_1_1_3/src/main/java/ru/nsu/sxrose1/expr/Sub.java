package ru.nsu.sxrose1.expr;

import java.util.function.BinaryOperator;

public final class Sub extends BinaryExpression {
  /**
   * @param lhs Left hand side of expression.
   * @param rhs Right hand side of expression.
   */
  public Sub(Expression lhs, Expression rhs) {
    super(lhs, rhs);
  }

  /** {@inheritDoc} */
  @Override
  public BinaryOperator<Double> operator() {
    return (a, b) -> a - b;
  }

  /** {@inheritDoc} */
  @Override
  protected Expression simplifyCancelOutInplace() {
    if (rhs instanceof Number) {
      // We don't handle the rhs == lhs case here
      // because it will be just expression eval and will be handled in simplify logic.
      if (((Number) rhs).value == 0.0) return lhs;
      return this;
    }

    if (lhs.exprEquals(rhs)) return new Number(0.0);

    return this;
  }

  /** {@inheritDoc} */
  @Override
  protected String repr() {
    return "-";
  }

  /** {@inheritDoc} */
  @Override
  public Expression derivative(String variable) {
    return new Sub(lhs.derivative(variable), rhs.derivative(variable));
  }
}
