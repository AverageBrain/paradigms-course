package expression.generic.evaluate;

import java.math.BigInteger;

public class BigIntegerEvaluate implements Evaluate<BigInteger> {
    @Override
    public BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger subtract(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public BigInteger multiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public BigInteger divide(BigInteger left, BigInteger right) {
        return left.divide(right);
    }

    @Override
    public BigInteger min(BigInteger left, BigInteger right) {
        return left.min(right);
    }

    @Override
    public BigInteger max(BigInteger left, BigInteger right) {
        return left.max(right);
    }

    @Override
    public BigInteger abs(BigInteger operand) {
        return operand.abs();
    }

    @Override
    public BigInteger negate(BigInteger operand) {
        return operand.negate();
    }

    @Override
    public BigInteger count(BigInteger operand) {
        return BigInteger.valueOf(operand.bitCount());
    }

    @Override
    public BigInteger getNumber(String strNum) {
        return new BigInteger(strNum);
    }
}

