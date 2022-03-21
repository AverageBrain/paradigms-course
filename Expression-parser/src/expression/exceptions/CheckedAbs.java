package expression.exceptions;

import expression.Abs;
import expression.PartOfExpression;

public class CheckedAbs extends Abs {
    public CheckedAbs(PartOfExpression operand) {
        super(operand);
    }

    @Override
    public int calculate(int value) throws OverflowException {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow by unary minus");
        }
        return super.calculate(value);
    }
}
