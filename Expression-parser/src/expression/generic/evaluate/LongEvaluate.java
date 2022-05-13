package expression.generic.evaluate;

public class LongEvaluate implements Evaluate<Long> {
    @Override
    public Long add(Long left, Long right) {
        return left + right;
    }

    @Override
    public Long subtract(Long left, Long right) {
        return left - right;
    }

    @Override
    public Long multiply(Long left, Long right) {
        return left * right;
    }

    @Override
    public Long divide(Long left, Long right) {
        return left / right;
    }

    @Override
    public Long min(Long left, Long right) {
        return Long.min(left, right);
    }

    @Override
    public Long max(Long left, Long right) {
        return Long.max(left, right);
    }

    @Override
    public Long abs(Long operand) {
        return ((operand > 0)? operand: -operand);
    }

    @Override
    public Long negate(Long operand) {
        return -operand;
    }

    @Override
    public Long getNumber(String strNum) {
        return Long.valueOf(strNum);
    }

    @Override
    public Long count(Long operand) {
        return (long) Long.bitCount(operand);
    }
}
