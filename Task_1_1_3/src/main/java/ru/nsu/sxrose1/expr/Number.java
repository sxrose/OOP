package ru.nsu.sxrose1.expr;

public class Number extends Expression {
  public double value;

  /**
   * @param value value of number expression.
   */
  public Number(double value) {
    this.value = value;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return Double.toString(value);
  }

  /** {@inheritDoc} */
  @Override
  public Expression simplify() {
    return this;
  }
}
