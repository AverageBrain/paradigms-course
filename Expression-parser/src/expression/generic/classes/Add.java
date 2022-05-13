package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public class Add<T extends Number> extends BinaryOperation<T> {
    public Add(PartOfExpression<T> leftPart, PartOfExpression<T> rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public String getSign() {
        return "+";
    }

    @Override
    public boolean getAssoc() {
        return true;
    }

    @Override
    public int getPrior() {
        return 1;
    }

    @Override
    public T calculate(T leftValue, T rightValue, Evaluate<T> eval) {
        return eval.add(leftValue, rightValue);
    }
}


