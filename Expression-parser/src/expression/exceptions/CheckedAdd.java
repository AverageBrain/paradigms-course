package expression.exceptions;

import expression.Add;
import expression.PartOfExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public int calculate(int leftValue, int rightValue) throws ArithmExceptions {
        if ((leftValue > 0 && rightValue > 0 && leftValue + rightValue <= 0) ||
                (leftValue < 0 && rightValue < 0 && leftValue + rightValue >= 0)) {
            throw new OverflowException("Overflow by add");
        }
        return leftValue + rightValue;
    }
}
