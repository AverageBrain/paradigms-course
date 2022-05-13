package expression.generic.evaluate;

import javax.swing.*;

public class Truncation10Evaluate implements Evaluate<Integer> {
    private Integer truncation(Integer value) {
        return value / 10 * 10;
    }

    @Override
    public Integer add(Integer left, Integer right) {
        return truncation(left + right);
    }

    @Override
    public Integer subtract(Integer left, Integer right) {
        return truncation(left - right);
    }

    @Override
    public Integer multiply(Integer left, Integer right) {
        return truncation(left * right);
    }

    @Override
    public Integer divide(Integer left, Integer right) {
        return truncation(left / right);
    }

    @Override
    public Integer min(Integer left, Integer right) {
        return truncation(Integer.min(left, right));
    }

    @Override
    public Integer max(Integer left, Integer right) {
        return truncation(Integer.max(left, right));
    }

    @Override
    public Integer abs(Integer operand) {
        return truncation((operand >= 0)? operand: -operand);
    }

    @Override
    public Integer negate(Integer operand) {
        return truncation(-operand);
    }

    @Override
    public Integer getNumber(String strNum) {
        return truncation(Integer.parseInt(strNum));
    }

    @Override
    public Integer count(Integer operand) {
        return truncation(Integer.bitCount(operand));
    }
}
