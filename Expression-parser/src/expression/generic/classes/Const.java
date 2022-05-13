package expression.generic.classes;

import expression.generic.evaluate.Evaluate;

public class Const<T extends Number> implements PartOfExpression<T> {
    private final String cnst;

    @Override
    public int getPrior() {
        return 5;
    }

    public Const(String strNum) {
        this.cnst = strNum;
    }

    public T getNumber(Evaluate<T> eval) {
        return eval.getNumber(cnst);
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluate<T> eval) {
        return eval.getNumber(cnst);
    }

    @Override
    public String toString() {
        return cnst.toString();
    }

    @Override
    public String toMiniString() {
        return cnst.toString();
    }
}
