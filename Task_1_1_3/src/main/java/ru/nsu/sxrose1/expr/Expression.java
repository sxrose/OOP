package ru.nsu.sxrose1.expr;

public abstract class Expression implements Cloneable {
  /**
   * Indicates whether some other expression is equal to this one. Two expressions are the same if
   * and only if they are the same object or structurally constitute the same expression tree up to
   * associativity.
   *
   * @param other the expression with which to compare
   * @return true, if object is Expression and is equal to this one.
   * @see Expression#equals(Object)
   */
  public abstract boolean exprEquals(Expression other);

  /**
   * Indicates whether some other object is "equal to" this expression. Two expressions are the same
   * if and only if they are the same object or structurally constitute the same expression tree up
   * to associativity.
   *
   * @param other the reference object with which to compare
   * @return true, if object is Expression and is equal to this one.
   * @see Expression#exprEquals(Expression)
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    return (other instanceof Expression) && this.exprEquals((Expression) other);
  }

  /**
   * @return height of expression tree.
   */
  public abstract int height();

  /** Returns a string representation of the expression. */
  @Override
  public abstract String toString();

  /** {@inheritDoc} */
  @Override
  public abstract int hashCode();

  /**
   * Simplifies expression in arithmetic sense. Creates new Expression distinct from this.
   *
   * @return new simplified Expression.
   */
  public abstract Expression simplify();

  /**
   * Make new expression representing derivative with respect to some variable.
   *
   * @param variable string representing variable with respect to which derivative is taken.
   * @return Expression representing derivative.
   */
  public abstract Expression derivative(String variable);

  /**
   * Performs shallow copy of Expression.
   *
   * @return shallow copy of this.
   */
  public final Expression shallowCopy() {
    try {
      return (Expression) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  /**
   * Performs deep copy of Expression.
   *
   * @return deep copy of this.
   */
  @Override
  public Expression clone() {
    return this.shallowCopy();
  }
}
