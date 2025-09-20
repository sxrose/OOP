package ru.nsu.sxrose1.expr;

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
  protected String getOpSign() {
    return "/";
  }

  /** {@inheritDoc} */
  @Override
  public Expression simplify() {
    // TODO: write simplify for Div
    return this;
  }
}
