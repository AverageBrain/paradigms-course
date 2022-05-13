package expression.generic.evaluate;

public class DoubleEvaluate implements Evaluate<Double> {
    @Override
    public Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double multiply(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double divide(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double min(Double left, Double right) {
        return Double.min(left, right);
    }

    @Override
    public Double max(Double left, Double right) {
        return Double.max(left, right);
    }

    @Override
    public Double abs(Double operand) {
        return ((operand >= 0)? operand: -operand);
    }

    @Override
    public Double negate(Double operand) {
        return -operand;
    }

    @Override
    public Double getNumber(String strNum) {
        return Double.valueOf(strNum);
    }

    @Override
    public Double count(Double operand) {
        Long analog = Double.doubleToLongBits(operand);
        return (double) Long.bitCount(analog);
    }
}
