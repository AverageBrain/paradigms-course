package expression.exceptions;

import expression.PartOfExpression;
import expression.ShiftR;

public class CheckedShiftR extends ShiftR {
    public CheckedShiftR(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    public int calculate(int leftValue, int rightValue) throws ArithmExceptions {
//        if (rightValue < 0) {
//            throw new ShiftException("Incorrect shift by right shift");
//        }
        return super.calculate(leftValue, rightValue);
    }
}
