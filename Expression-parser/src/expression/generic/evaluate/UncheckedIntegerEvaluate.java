package expression.generic.evaluate;

public class UncheckedIntegerEvaluate implements Evaluate<Integer> {

    @Override
    public Integer add(Integer left, Integer right) {
        return left + right;
    }

    @Override
    public Integer subtract(Integer left, Integer right) {
        return left - right;
    }

    @Override
    public Integer multiply(Integer left, Integer right) {
        return left * right;
    }

    @Override
    public Integer divide(Integer left, Integer right) {
        return left / right;
    }

    @Override
    public Integer min(Integer left, Integer right) {
        return Integer.min(left, right);
    }

    @Override
    public Integer max(Integer left, Integer right) {
        return Integer.max(left, right);
    }

    @Override
    public Integer abs(Integer operand) {
        return ((operand >= 0)? operand: -operand);
    }

    @Override
    public Integer negate(Integer operand) {
        return -operand;
    }

    @Override
    public Integer getNumber(String strNum) {
        return Integer.valueOf(strNum);
    }

    @Override
    public Integer count(Integer operand) {
        return Integer.bitCount(operand);
    }
}
