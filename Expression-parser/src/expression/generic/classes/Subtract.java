package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public class Subtract<T extends Number> extends BinaryOperation<T> {
    public Subtract(PartOfExpression<T> leftPart, PartOfExpression<T> rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public String getSign() {
        return "-";
    }

    @Override
    public boolean getAssoc() {
        return false;
    }

    @Override
    public int getPrior() {
        return 1;
    }

    @Override
    public T calculate(T leftValue, T rightValue, Evaluate<T> eval) {
        return eval.subtract(leftValue, rightValue);
    }
}
