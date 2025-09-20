package ru.nsu.sxrose1.expr;

public abstract class AssociativeBinaryExpression extends BinaryExpression {
  /**
   * @param lhs Left hand side of expression.
   * @param rhs Right hand side of expression.
   */
  public AssociativeBinaryExpression(Expression lhs, Expression rhs) {
    super(lhs, rhs);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAssociative() {
    return true;
  }
}
