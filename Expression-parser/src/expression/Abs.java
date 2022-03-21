package expression;

public class Abs extends UnaryOperation {
    protected Abs(PartOfExpression operand) {
        super(operand);
    }

    @Override
    public String getSign() {
        return "abs";
    }

    @Override
    public int calculate(int value) {
        if (value < 0) {
            return -value;
        }
        return value;
    }
}
