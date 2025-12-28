package ru.nsu.sxrose1.expr;

import java.util.function.BinaryOperator;

/** Represents E + E expressions. */
public final class Add extends AssociativeBinaryExpression {
    /**
     * Constructs addition expression.
     *
     * @param lhs Left hand side of expression.
     * @param rhs Right hand side of expression.
     */
    public Add(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    /** {@inheritDoc} */
    @Override
    public BinaryOperator<Double> operator() {
        return Double::sum;
    }

    /** {@inheritDoc} */
    @Override
    public double identity() {
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    protected String opRepr() {
        return "+";
    }

    /** {@inheritDoc} */
    @Override
    public Expression derivative(String variable) {
        return new Add(lhs.derivative(variable), rhs.derivative(variable));
    }
}
