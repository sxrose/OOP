package ru.nsu.sxrose1.expr;

public abstract class Expression {
  /** Returns a string representation of the expression. */
  @Override
  public abstract String toString();

  /** Prints out representation of expression to standard output. */
  public void print() {
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
}
