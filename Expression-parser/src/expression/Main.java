package expression;

import expression.exceptions.ExpressionParser;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        PartOfExpression expr = new ShiftA(
                new Variable("x"), new Negate(new Variable("x"))
        );
        System.out.println(expr.toString());
        System.out.println(expr.toMiniString());
        String minusOneTest = "-2147483648 + -1";
        String zeroTest = "(((y + x) - -2147483648) + (x + x))";
        String firstTest = "x*y+(z-1   )/10";
        String secondTest = "x--y--z";
        String thirdTest = "12 + x * x / y / (- z) * (5 + 34) / (23) + (x + -y)";
        String fourthTest = "(((y << 1088686421) >>> (-1326032329 >>> 475563971)) << ((y / -497099380) / 778623607))";
        String fifthTest = "((z + z) + (1169916708 - -789160982))";
        String sixthTest = "-0";

//        String[] tests = new String[]{minusOneTest, zeroTest, firstTest, secondTest, thirdTest, fourthTest, fifthTest};
//        for (int i = 0; i < tests.length; i++) {
//            test(tests[i], false);
//            System.out.println();
//        }
        String seventhTest = "(0 A   \n" +
                " \n" +
                " + 1)";
        //test(seventhTest, false);
        test(sixthTest, false);
//        PartOfExpression checkLog = new Pow(
//                new Const(32),
//                new Const(2)
//        );
//        System.out.println(checkLog.evaluate(1));

    }

    public static final void test(String test, boolean withEvaluate) {
        System.out.println(test);
        PartOfExpression parse = new ExpressionParser().parse(test);
        System.out.println(parse.toString());
        System.out.println(parse.toMiniString());
        if (withEvaluate) {
            System.out.println(parse.evaluate(2, 2, 0));
        }
    }
}
