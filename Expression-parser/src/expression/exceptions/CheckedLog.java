package expression.exceptions;

import expression.Log;
import expression.PartOfExpression;

public class CheckedLog extends Log {
    public CheckedLog(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public int calculate(int leftValue, int rightValue) throws ArithmExceptions {
        if (((rightValue <= 0 || rightValue == 1) || leftValue <= 0)) {
            if (rightValue <= 0 || rightValue == 1) {
                throw new LogarithmException("Incorrect base by logarithm");
            }
            throw new LogarithmException("Incorrect argument by logarithm");
        }
        return super.calculate(leftValue, rightValue);
    }
}
