package ru.nsu.sxrose1.expr;

public abstract class Expression {
    /**
     * Returns a string representation of the expression.
     */
    @Override
    public abstract String toString();

    /**
     * Prints out representation of expression to standard output.
     */
    public void print() {
        System.out.print(this.toString());
    }

    /**
     * Simplifies expression in arithmetic sense.
     *
     * @return simplified Expression
     */
    public abstract Expression simplify();
}
