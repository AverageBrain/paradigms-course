package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public abstract class UnaryOperation<T extends Number> implements PartOfExpression<T> {
    private final PartOfExpression<T> operand;

    protected UnaryOperation(PartOfExpression <T> operand) {
        this.operand = operand;
    }

    abstract public String getSign();

    public int getPrior() {
        return 4;
    }

    abstract public T calculate(T value, Evaluate<T> eval);

    public PartOfExpression<T> getOperand() {
        return operand;
    }

//    public int evaluate(int value) {
//        return calculate(operand.evaluate(value), new IntegerEvaluator());
//    }
//
//    public int evaluate(int x, int y, int z) {
//        return calculate(operand.evaluate(x, y, z), new IntegerEvaluator());
//    }

    public T evaluate(T x, T y, T z, Evaluate<T> eval) {
        return calculate(operand.evaluate(x, y, z, eval), eval);
    }

    public String toString() {
        return getSign() + '(' + operand.toString() + ')';
    }

    private String withBrackets(PartOfExpression <T> expr) {
        return '(' + operand.toMiniString() + ')';
    }

    private boolean isConstOrVariable(PartOfExpression <T> expr) {
        return (expr instanceof Const || expr instanceof Variable);
    }

    @Override
    public String toMiniString() {
        if (isConstOrVariable(operand) || operand instanceof UnaryOperation) {
            return getSign() + ' ' + operand.toMiniString();
        } else {
            return getSign() + withBrackets(operand);
        }
    }
}
