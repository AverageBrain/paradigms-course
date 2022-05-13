package expression.generic.evaluate;

public interface Evaluate<T extends Number> {
    T add(T left, T right);
    T subtract(T left, T right);
    T multiply(T left, T right);
    T divide(T left, T right);
//    T pow(T left, T right);
//    T log(T left, T right);
//    T shiftL(T left, T right);
//    T shiftR(T left, T right);
//    T shiftA(T left, T right);
    T min(T left, T right);
    T max(T left, T right);
    T abs(T operand);
    T negate(T operand);
    T getNumber(String strNum);
    T count(T operand);
}
