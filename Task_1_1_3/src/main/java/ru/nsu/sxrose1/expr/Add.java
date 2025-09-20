package ru.nsu.sxrose1.expr;

public final class Add extends BinaryExpression {
  public Add(Expression lhs, Expression rhs) {
    super(lhs, rhs);
  }

  /** {@inheritDoc} */
  @Override
  protected String getOpSign() {
    return "+";
  }

  /** {@inheritDoc} */
  @Override
  public Expression simplify() {
    // TODO: write simplify for Add
    return this;
  }
}
