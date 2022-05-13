package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public abstract class BinaryOperation <T extends Number> implements PartOfExpression<T> {
    private final PartOfExpression<T> leftPart;
    private final PartOfExpression<T> rightPart;

    public BinaryOperation(PartOfExpression<T> leftPart, PartOfExpression<T> rightPart) {
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }

    abstract public String getSign();

    abstract public boolean getAssoc();

    abstract public T calculate(T leftValue, T rightValue, Evaluate<T> eval);

    public PartOfExpression<T> getLeftPart() {
        return leftPart;
    }

    public PartOfExpression<T> getRightPart() {
        return rightPart;
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluate<T> eval) {
        T leftValue = leftPart.evaluate(x, y, z, eval);
        T rightValue = rightPart.evaluate(x, y, z, eval);
        return calculate(leftValue, rightValue, eval);
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
                    (rightPart.getPrior() == this.getPrior() && this.getPrior() >= 2 && !((BinaryOperation<T>) rightPart).getAssoc())) {
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
}
