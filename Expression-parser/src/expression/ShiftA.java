package expression;
public class ShiftA extends BinaryOperation {
    public ShiftA(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public String getSign() {
        return ">>>";
    }

    @Override
    public boolean getAssoc() {
        return false;
    }

    @Override
    public int getPrior() {
        return 0;
    }

    @Override
    public int calculate(int leftValue, int rightValue) {
        return (leftValue >>> rightValue);
    }
}
