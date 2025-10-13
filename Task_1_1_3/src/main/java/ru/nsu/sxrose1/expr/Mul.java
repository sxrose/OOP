package ru.nsu.sxrose1.expr;

import java.util.Map;
import java.util.function.BinaryOperator;

public final class Mul extends AssociativeBinaryExpression {
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

  /** {@inheritDoc} */
  @Override
  protected Expression simplifyCancelOutInplace() {
    Expression simpl = super.simplifyCancelOutInplace();

    if (simpl instanceof Mul m) {
      if (m.lhs instanceof Number) {
        if (((Number) m.lhs).value == 0.0) return m.lhs;
      }

      if (m.rhs instanceof Number) {
        if (((Number) m.rhs).value == 0.0) return m.rhs;
      }
    }

    return simpl;
  }

  /** {@inheritDoc} */
  @Override
  public Expression derivative(String variable) {
    return new Add(
        new Mul(lhs.clone(), rhs.derivative(variable)),
        new Mul(lhs.derivative(variable), rhs.clone()));
  }
}
