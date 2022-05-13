package expression.generic.classes;

import expression.ToMiniString;
import expression.generic.evaluate.Evaluate;

public interface PartOfExpression<T extends Number> extends ToMiniString {
    public int getPrior();

    public T evaluate(T x, T y, T z, Evaluate<T> eval);
}
