package expression.generic.evaluate;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntegerEvaluate implements Evaluate<Integer> {

    @Override
    public Integer add(Integer left, Integer right) {
        if ((left > 0 && right > 0 && left + right <= 0) ||
                (left < 0 && right < 0 && left + right >= 0)) {
            throw new OverflowException("Overflow by add");
        }
        return left + right;
    }

    @Override
    public Integer subtract(Integer left, Integer right) {
        if ((left > 0 && right < 0 && left - right <= 0) ||
                (left < 0 && right > 0 && left - right >= 0) ||
                (left == 0 && right == Integer.MIN_VALUE)) {
            throw new OverflowException("Overflow subtract");
        }
        return left - right;
    }

    @Override
    public Integer multiply(Integer left, Integer right) {
        int res = left * right;
        if ((left != 0 && res / left != right) ||
                (right != 0 && res / right != left)) {
            throw new OverflowException("Overflow by multiply");
        }
        return res;
    }

    @Override
    public Integer divide(Integer left, Integer right) {
        if (right == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new OverflowException("Overflow by division");
        }
        return left / right;
    }

//    @Override
//    public Integer pow(Integer left, Integer right) {
//        if (left == 0 && right == 0) {
//            throw new ArithmExceptions("0 ** 0 error");
//        }
//        if (right < 0) {
//            throw new ArithmExceptions("Negation exponent by pow");
//        }
//        int ans = 1;
//        while (right > 0) {
//            if (right % 2 == 1) {
//                ans = multiply(ans, left);
//                right--;
//            } else {
//                left = multiply(left, left);
//                right /= 2;
//            }
//        }
//        return ans;
//    }
//
//    @Override
//    public Integer log(Integer left, Integer right) {
//        if (((right <= 0 || right == 1) || left <= 0)) {
//            if (right <= 0 || right == 1) {
//                throw new LogarithmException("Incorrect base by logarithm");
//            }
//            throw new LogarithmException("Incorrect argument by logarithm");
//        }
//        int res = 1, base = 0;
//        while (res * right <= left) {
//            int mulRes = res * right;
//            if (res != 0 && mulRes / res != right) {
//                break; // overflow by multiply
//            }
//            res = mulRes;
//            base++;
//        }
//        return base;
//    }

//    @Override
//    public Integer shiftL(Integer left, Integer right) {
//        return (left << right);
//    }
//
//    @Override
//    public Integer shiftR(Integer left, Integer right) {
//        return (left >> right);
//    }
//
//    @Override
//    public Integer shiftA(Integer left, Integer right) {
//        return (left >>> right);
//    }

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
        if (operand == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow by unary minus");
        }
        return ((operand >= 0)? operand: -operand);
    }

    @Override
    public Integer negate(Integer operand) {
        if (operand == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow by unary minus");
        }
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
