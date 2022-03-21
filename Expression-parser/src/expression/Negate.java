package expression;

import expression.PartOfExpression;
import expression.UnaryOperation;

public class Negate extends UnaryOperation {
    public Negate(PartOfExpression operand) {
        super(operand);
    }

    @Override
    public String getSign() {
        return "-";
    }

    @Override
    public int calculate(int value) {
        return -value;
    }
}
