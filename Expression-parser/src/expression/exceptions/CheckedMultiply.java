package expression.exceptions;

import expression.Multiply;
import expression.PartOfExpression;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public int calculate(int leftValue, int rightValue) throws ArithmExceptions {
        int res = leftValue * rightValue;
        if ((leftValue != 0 && res / leftValue != rightValue) ||
                (rightValue != 0 && res / rightValue != leftValue)) {
            throw new OverflowException("Overflow by multiply");
        }
        return res;
    }
}
