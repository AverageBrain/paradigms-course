package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public class Abs<T extends Number> extends UnaryOperation<T> {
    public Abs(PartOfExpression<T> operand) {
        super(operand);
    }

    @Override
    public String getSign() {
        return "abs";
    }

    @Override
    public T calculate(T value, Evaluate<T> eval) {
        return eval.abs(value);
    }
}
