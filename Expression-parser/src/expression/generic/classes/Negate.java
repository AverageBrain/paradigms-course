package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public class Negate<T extends Number> extends UnaryOperation<T> {
    public Negate(PartOfExpression<T> operand) {
        super(operand);
    }

    @Override
    public String getSign() {
        return "-";
    }

    @Override
    public T calculate(T operand, Evaluate<T> eval) {
        return eval.negate(operand);
    }
}
