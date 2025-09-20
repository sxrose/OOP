package ru.nsu.sxrose1.expr;

public class Variable extends Expression {
    public String name;

    public Variable(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression simplify() {
        return this;
    }
}
