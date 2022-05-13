package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

import java.util.InputMismatchException;

public class Variable<T extends Number> implements PartOfExpression<T> {
    private final String variable;

    public Variable(String value) {
        this.variable = value;
    }

    @Override
    public int getPrior() {
        return 5;
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluate<T> eval) {
        return switch (variable) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new InputMismatchException("Incorrect name of variable" + getVariable());
        };
    }

    @Override
    public String toString() {
        return getVariable();
    }

    @Override
    public String toMiniString() {
        return this.toString();
    }
}
