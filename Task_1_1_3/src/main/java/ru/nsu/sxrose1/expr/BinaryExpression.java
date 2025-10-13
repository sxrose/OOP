package ru.nsu.sxrose1.expr;

import java.util.Objects;
import java.util.function.BinaryOperator;

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

  /** {@inheritDoc} */
  @Override
  public boolean exprEquals(Expression other) {
    return this.getClass() == other.getClass()
        && this.lhs.equals(((BinaryExpression) other).lhs)
        && this.rhs.equals(((BinaryExpression) other).rhs);
  }

  /** {@inheritDoc} */
  @Override
  public int height() {
    return Integer.max(lhs.height(), rhs.height()) + 1;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(lhs, rhs, opRepr());
  }

  /**
   * Returns true if Expression is associative. Default is false for BinaryExpression, redefined to
   * true in AssociativeBinaryExpression.
   *
   * @return true if Expression is associative, false otherwise.
   */
  public boolean isAssociative() {
    return false;
  }

  /**
   * @return functional representation of binary expression's operator.
   */
  public abstract BinaryOperator<Double> operator();

  /**
   * Simplifies lhs and rhs in-place.
   *
   * @return simplified expression.
   */
  protected abstract Expression simplifyCancelOutInplace();

  /** {@inheritDoc} */
  @Override
  public Expression simplify() {
    BinaryExpression simpl = this.clone();

    simpl.lhs = lhs.simplify();
    simpl.rhs = rhs.simplify();
    Expression simpl2 = simpl.simplifyCancelOutInplace();

    return switch (simpl2) {
      case BinaryExpression be -> {
        if (be.lhs instanceof Number && be.rhs instanceof Number) {
          double lhsVal = ((Number) be.lhs).value;
          double rhsVal = ((Number) be.rhs).value;
          double res = be.operator().apply(lhsVal, rhsVal);
          yield new Number(res);
        }

        yield simpl2;
      }
      default -> simpl2;
    };
  }

  /**
   * @return String representation of operation's sign.
   */
  protected abstract String opRepr();

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "(" + String.join(" " + opRepr() + " ", lhs.toString(), rhs.toString()) + ")";
  }

  /** {@inheritDoc} */
  @Override
  public final BinaryExpression clone() {
    BinaryExpression ret = (BinaryExpression) super.shallowCopy();

    ret.lhs = ret.lhs.clone();
    ret.rhs = ret.rhs.clone();

    return ret;
  }
}
