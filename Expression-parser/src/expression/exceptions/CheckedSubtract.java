package expression.exceptions;

import expression.PartOfExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public int calculate(int leftValue, int rightValue) throws OverflowException {
        if ((leftValue > 0 && rightValue < 0 && leftValue - rightValue <= 0) ||
                (leftValue < 0 && rightValue > 0 && leftValue - rightValue >= 0) ||
                (leftValue == 0 && rightValue == Integer.MIN_VALUE)) {
            throw new OverflowException("Overflow subtract");
        }
        return leftValue - rightValue;
    }
}
