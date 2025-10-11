package ru.nsu.sxrose1.expr;

public abstract class Expression implements Cloneable {
  public static int SIMPLIFICATION_MAX_ITERATIONS = 2048;

  /**
   * Indicates whether some other expression is equal to this one. Two expressions are the same if
   * and only if they are the same object or structurally constitute the same expression tree up to
   * associativity.
   *
   * @param other the expression with which to compare
   * @return {@code true}, if {@code object} is @{code Expression} and is equal to this one.
   * @see Expression#equals(Object)
   */
  public abstract boolean exprEquals(Expression other);

  /**
   * Indicates whether some other object is "equal to" this expression. Two expressions are the same
   * if and only if they are the same object or structurally constitute the same expression tree up
   * to associativity.
   *
   * @param other the reference object with which to compare
   * @return {@code true}, if {@code object} is @{code Expression} and is equal to this one.
   * @see Expression#exprEquals(Expression)
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    return (other instanceof Expression) && this.exprEquals((Expression) other);
  }

  /** Returns a string representation of the expression. */
  @Override
  public abstract String toString();

  /** {@inheritDoc} */
  @Override
  public abstract int hashCode();

  /** Prints out representation of expression to standard output. */
  public final void print() {
    System.out.print(this.toString());
  }

  /**
   * Simplifies expression in arithmetic sense. Must return {@code this} reference if no
   * simplification is possible.
   *
   * @return new simplified {@code Expression} if simplification took place, {@code this} reference
   *     otherwise.
   */
  public abstract Expression simplify();

  /**
   * Creates simplified expression from {@code expr}, performing simplification until fixed-point
   * reaches or @{code Expression.SIMPLIFICATION_MAX_ITERATIONS} iterations exceeds.
   *
   * @see Expression::simplify.
   * @param expr Expression to simplify.
   * @return simplified expression.
   */
  public static Expression simplifyFixedPoint(Expression expr) {
    Expression simplifed = expr.clone();
    for (int i = 0; i < Expression.SIMPLIFICATION_MAX_ITERATIONS; i++) {
      Expression nextSimplified = simplifed.simplify();
      if (simplifed.exprEquals(nextSimplified)) break;
      simplifed = nextSimplified;
    }

    return simplifed;
  }

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
   * @return shallow copy of {@code this}.
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
   * @return deep copy of {@code this}.
   */
  @Override
  public Expression clone() {
    return this.shallowCopy();
  }
}
