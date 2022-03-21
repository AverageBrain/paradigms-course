package expression;

import expression.exceptions.OverflowException;

public class Pow extends BinaryOperation {
    public Pow(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public String getSign() {
        return "**";
    }

    @Override
    public boolean getAssoc() {
        return false;
    }

    @Override
    public int getPrior() {
        return 3;
    }

    @Override
    public int calculate(int leftValue, int rightValue) {
        int ans = 1;
        while (rightValue > 0) {
            if (rightValue % 2 == 1) {
                ans *= leftValue;
                rightValue--;
            } else {
                leftValue *=  leftValue;
                rightValue /= 2;
            }
        }
        return ans;
    }
}
