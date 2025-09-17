package ru.nsu.sxrose1.expr;

public abstract class BinaryExpression extends Expression {
  public Expression lhs;
  public Expression rhs;

  protected abstract String getOpSign();

  @Override
  public String toString() {
    return "(" + String.join(" " + getOpSign() + " ", lhs.toString(), rhs.toString()) + ")";
  }
}
