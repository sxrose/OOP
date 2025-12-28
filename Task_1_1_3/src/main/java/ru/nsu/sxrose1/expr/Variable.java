package ru.nsu.sxrose1.expr;

import java.util.Objects;

/** Represents singular variable expressions. */
public final class Variable extends Expression {
    public String name;

    /**
     * Constructs variable expression.
     *
     * @param name String representation of variable.
     */
    public Variable(String name) {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public boolean exprEquals(Expression other) {
        return (other instanceof Variable) && ((Variable) other).name.equals(this.name);
    }

    /** {@inheritDoc} */
    @Override
    public int height() {
        return 1;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /** {@inheritDoc} */
    @Override
    public Expression simplify() {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Expression derivative(String variable) {
        return variable.equals(this.name) ? new Number(1.0) : new Number(0.0);
    }
}
