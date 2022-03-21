package expression;

import java.util.InputMismatchException;

public abstract class  BinaryOperation implements PartOfExpression {
    private final PartOfExpression leftPart;
    private final PartOfExpression rightPart;

    public BinaryOperation(PartOfExpression leftPart, PartOfExpression rightPart) {
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }

    abstract public String getSign();

    abstract public boolean getAssoc();

    abstract public int calculate(int leftValue, int rightValue);

    public PartOfExpression getLeftPart() {
        return leftPart;
    }

    public PartOfExpression getRightPart() {
        return rightPart;
    }

    @Override
    public int evaluate(int value) {
        int leftValue = leftPart.evaluate(value);
        int rightValue = rightPart.evaluate(value);
        return calculate(leftValue, rightValue);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int leftValue = leftPart.evaluate(x, y, z);
        int rightValue = rightPart.evaluate(x, y, z);
        return calculate(leftValue, rightValue);
    }

    @Override
    public String toString() {
        String oper = getSign();
        return '(' + leftPart.toString() + ' ' + oper + ' ' + rightPart.toString() + ')';
    }

    @Override
    public String toMiniString() {
        StringBuilder res = new StringBuilder();
        if (leftPart.getPrior() < this.getPrior()) {
            res.append('(').append(leftPart.toMiniString()).append(')');
        } else {
            res.append(leftPart.toMiniString());
        }
        res.append(' ').append(getSign()).append(' ');

        if (getAssoc()) {
            if (rightPart.getPrior() < this.getPrior() ||
                    (rightPart.getPrior() == this.getPrior() && this.getPrior() >= 2 && !((BinaryOperation) rightPart).getAssoc())) {
                res.append('(').append(rightPart.toMiniString()).append(')');
            } else {
                res.append(rightPart.toMiniString());
            }
        } else {
            if (rightPart.getPrior() <= this.getPrior()) {
                res.append('(').append(rightPart.toMiniString()).append(')');
            } else {
                res.append(rightPart.toMiniString());
            }
        }
        return res.toString();
    }

    @Override
    public boolean equals(Object expr) {
        if (expr == this) return true;
        if (expr == null || getClass() != expr.getClass()) {
            return false;
        }
        BinaryOperation cur = (BinaryOperation) expr;
        return this.getLeftPart().equals((cur.getLeftPart())) && this.getRightPart().equals((cur.getRightPart()));
    }

    @Override
    public int hashCode() {
        return (this.getLeftPart().hashCode() * 1049 * 1049 + 1049 * this.getRightPart().hashCode()) + this.getClass().hashCode();
    }
}
