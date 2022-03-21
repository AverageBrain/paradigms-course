package expression;

import expression.BinaryOperation;
import expression.PartOfExpression;

public class Add extends BinaryOperation {
    public Add(PartOfExpression leftPart, PartOfExpression rightPart) {
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
    public int calculate(int leftValue, int rightValue) {
        return leftValue + rightValue;
    }
}


