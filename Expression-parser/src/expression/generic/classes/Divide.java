package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public class Divide<T extends Number> extends BinaryOperation<T> {
    public Divide(PartOfExpression<T> leftPart, PartOfExpression<T> rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public String getSign() {
        return "/";
    }

    @Override
    public boolean getAssoc() {
        return false;
    }

    @Override
    public int getPrior() {
        return 2;
    }

    @Override
    public T calculate(T leftValue, T rightValue, Evaluate<T> eval) {
        return eval.divide(leftValue, rightValue);
    }
}
