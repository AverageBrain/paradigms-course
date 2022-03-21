package expression.exceptions;

import expression.PartOfExpression;
import expression.ShiftL;

public class CheckedShiftL extends ShiftL {
    public CheckedShiftL(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    public int calculate(int leftValue, int rightValue) throws ArithmExceptions {
//        if (rightValue < 0) {
//            throw new ShiftException("Incorrect shift by left shift");
//        }
        return super.calculate(leftValue, rightValue);
    }
}
