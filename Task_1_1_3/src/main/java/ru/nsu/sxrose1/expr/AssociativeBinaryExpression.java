package ru.nsu.sxrose1.expr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/** Abstract class for all expressions of associative operations. */
public abstract class AssociativeBinaryExpression extends BinaryExpression {
    /**
     * Constructs associative binary expression.
     *
     * @param lhs Left hand side of expression.
     * @param rhs Right hand side of expression.
     */
    public AssociativeBinaryExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    /** {@inheritDoc} */
    @Override
    public boolean exprEquals(Expression other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }

        AssociativeBinaryExpression otherAssoc = (AssociativeBinaryExpression) other;

        if (this.lhs.equals(otherAssoc.lhs)) {
            return this.rhs.equals(otherAssoc.rhs);
        }
        if (this.lhs.equals(otherAssoc.rhs)) {
            return this.rhs.equals(otherAssoc.lhs);
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(new HashSet<Expression>(Arrays.asList(lhs, rhs)), opRepr());
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAssociative() {
        return true;
    }

    /**
     * @return identity element of associative binary operation.
     */
    public abstract double identity();

    /** {@inheritDoc} */
    @Override
    protected Expression simplifyCancelOutInplace() {
        if (lhs instanceof Number) {
            if (((Number) lhs).value == identity()) {
                return rhs;
            }
        }

        if (rhs instanceof Number) {
            if (((Number) rhs).value == identity()) {
                return lhs;
            }
        }

        return this;
    }
}
