package expression;

import expression.BinaryOperation;
import expression.PartOfExpression;

public class Divide extends BinaryOperation {
    public Divide(PartOfExpression leftPart, PartOfExpression rightPart) {
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
    public int calculate(int leftValue, int rightValue) {
        return leftValue / rightValue;
    }
}
