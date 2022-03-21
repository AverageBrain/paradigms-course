package expression.exceptions;

import expression.Divide;
import expression.PartOfExpression;

public class CheckedDivide extends Divide {
    public CheckedDivide(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public int calculate(int leftValue, int rightValue) throws ArithmExceptions {
        if (rightValue == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        if (leftValue == Integer.MIN_VALUE && rightValue == -1) {
            throw new OverflowException("Overflow by division");
        }
        return leftValue / rightValue;
    }
}
