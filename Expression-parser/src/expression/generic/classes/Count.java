package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public class Count<T extends Number> extends UnaryOperation<T> {
    public Count(PartOfExpression<T> operand) {
        super(operand);
    }

    @Override
    public String getSign() {
        return "count";
    }

    @Override
    public T calculate(T value, Evaluate<T> eval) {
        return eval.count(value);
    }
}
