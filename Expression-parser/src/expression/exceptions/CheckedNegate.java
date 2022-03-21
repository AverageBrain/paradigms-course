package expression.exceptions;

import expression.Negate;
import expression.PartOfExpression;

public class CheckedNegate extends Negate {
    public CheckedNegate(PartOfExpression operand) {
        super(operand);
    }

    @Override
    public int calculate(int value) throws ArithmExceptions {
        if (value == -2_147_483_648) {
            throw new OverflowException("Overflow by unary minus");
        }
        return -value;
    }
}
