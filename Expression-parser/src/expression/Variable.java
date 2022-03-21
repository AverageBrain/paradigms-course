package expression;

import java.util.InputMismatchException;

public class Variable implements PartOfExpression {
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
    public int evaluate(int value) {
        return value;
    }

    public int evaluate(int x, int y, int z) {
        switch (variable) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                throw new InputMismatchException("Incorrect name of variable" + getVariable());
        }
    }

    @Override
    public String toString() {
        return getVariable();
    }

    @Override
    public String toMiniString() {
        return this.toString();
    }

    @Override
    public boolean equals(Object expr) {
        if (this == expr) return true;
        if (expr == null || getClass() != expr.getClass()) return false;
        Variable curVariable = (Variable) expr;
        return this.getVariable().equals(curVariable.getVariable());
    }

    @Override
    public int hashCode() {
        return this.getVariable().hashCode();
    }


}
