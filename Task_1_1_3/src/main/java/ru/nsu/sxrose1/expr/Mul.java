package ru.nsu.sxrose1.expr;

public class Mul extends BinaryExpression {
  /**
   * @param lhs Left hand side of expression.
   * @param rhs Right hand side of expression.
   */
  public Mul(Expression lhs, Expression rhs) {
    super(lhs, rhs);
  }

  /** {@inheritDoc} */
  @Override
  protected String getOpSign() {
    return "*";
  }

  /** {@inheritDoc} */
  @Override
  public Expression simplify() {
    // TODO: write simplify for Mul
    return this;
  }
}
