package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public class Max<T extends Number> extends BinaryOperation<T> {
    public Max(PartOfExpression<T> leftPart, PartOfExpression<T> rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public String getSign() {
        return "max";
    }

    @Override
    public boolean getAssoc() {
        return true;
    }

    @Override
    public T calculate(T leftValue, T rightValue, Evaluate<T> eval) {
        return eval.max(leftValue, rightValue);
    }

    @Override
    public int getPrior() {
        return 0;
    }
}
