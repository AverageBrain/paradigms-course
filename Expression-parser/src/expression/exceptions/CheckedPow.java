package expression.exceptions;

import expression.Multiply;
import expression.PartOfExpression;
import expression.Pow;

public class CheckedPow extends Pow {
    public CheckedPow(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    public int checkedMultiply(int l, int r) {
        return new CheckedMultiply(null, null).calculate(l, r);
    }

    @Override
    public int calculate(int leftValue, int rightValue) throws ArithmExceptions {
        if (leftValue == 0 && rightValue == 0) {
            throw new ArithmExceptions("0 ** 0 error");
        }
        if (rightValue < 0) {
            throw new ArithmExceptions("Negation exponent by pow");
        }
        int ans = 1;
        while (rightValue > 0) {
            if (rightValue % 2 == 1) {
                ans = checkedMultiply(ans, leftValue);
                rightValue--;
            } else {
                leftValue = checkedMultiply(leftValue, leftValue);
                rightValue /= 2;
            }
        }
        return ans;
    }
}
