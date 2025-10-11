package ru.nsu.sxrose1.expr;

import java.util.Objects;

public final class Number extends Expression {
  public double value;

  /**
   * @param value value of number expression.
   */
  public Number(double value) {
    this.value = value;
  }

  /** {@inheritDoc} */
  @Override
  public boolean exprEquals(Expression other) {
    return (other instanceof Number) && (((Number) other).value == this.value);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return Double.toString(value);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  /** {@inheritDoc} */
  @Override
  public Expression simplify() {
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Expression derivative(String variable) {
    return new Number(0.0);
  }
}
