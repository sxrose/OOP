package ru.nsu.sxrose1.expr;

public final class Add extends BinaryExpression {
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOpSign() {
        return "+";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression simplify() {
        return null;
    }


}
