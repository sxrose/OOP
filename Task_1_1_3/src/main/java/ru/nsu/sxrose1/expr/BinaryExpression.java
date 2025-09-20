package ru.nsu.sxrose1.expr;

public abstract class BinaryExpression extends Expression {
  public Expression lhs;
  public Expression rhs;

  /**
   * @param lhs Left hand side of expression.
   * @param rhs Right hand side of expression.
   */
  public BinaryExpression(Expression lhs, Expression rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }

  /**
   * Returns {@code true} if Expression is associative. Default is {@code false} for {@code
   * BinaryExpression}, redefined to {@code true} in {@code AssociativeBinaryExpression}
   *
   * @return {@code true} if Expression is associative, otherwise @{code false}.
   */
  public boolean isAssociative() {
    return false;
  }

  /**
   * Returns string representation of operation's sign.
   *
   * @return String representation of operation's sign..
   */
  protected abstract String getOpSign();

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "(" + String.join(" " + getOpSign() + " ", lhs.toString(), rhs.toString()) + ")";
  }
}
