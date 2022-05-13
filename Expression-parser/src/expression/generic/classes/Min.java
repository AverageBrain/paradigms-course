package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public class Min<T extends Number> extends BinaryOperation<T> {
    public Min(PartOfExpression<T> leftPart, PartOfExpression<T> rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public String getSign() {
        return "min";
    }

    @Override
    public boolean getAssoc() {
        return true;
    }

    @Override
    public T calculate(T leftValue, T rightValue, Evaluate<T> eval) {
        return eval.min(leftValue, rightValue);
    }

    @Override
    public int getPrior() {
        return 0;
    }
}
