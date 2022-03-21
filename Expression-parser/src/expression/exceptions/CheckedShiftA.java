package expression.exceptions;

import expression.PartOfExpression;
import expression.ShiftA;

public class CheckedShiftA extends ShiftA {
    public CheckedShiftA(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public int calculate(int leftValue, int rightValue) throws ArithmExceptions {
//        if (rightValue < 0) {
//            throw new ShiftException("Incorrect shift by arithmetic shift");
//        }
            return super.calculate(leftValue, rightValue);
    }
}
