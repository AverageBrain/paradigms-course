package expression.generic;

import expression.generic.classes.PartOfExpression;
import expression.generic.evaluate.*;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    Map<String, Evaluate<? extends Number>> MODES = Map.of(
            "i", new IntegerEvaluate(),
            "d", new DoubleEvaluate(),
            "bi",new BigIntegerEvaluate(),
            "u", new UncheckedIntegerEvaluate(),
            "l", new LongEvaluate(),
            "t", new Truncation10Evaluate()
    );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2)
            throws Exception {
        return fillTable(MODES.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    public <T extends Number> Object[][][] fillTable(Evaluate<T> eval, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        Object[][][] res = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        PartOfExpression<T> expr = new ExpressionGenericParser<T>().parse(expression);
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        res[i][j][k] = expr.evaluate(eval.getNumber(String.valueOf(x1 + i)),
                                eval.getNumber(String.valueOf(y1 + j)),
                                eval.getNumber(String.valueOf(z1 + k)), eval);

                    } catch (Exception ignored) {

                    }
                }
            }
        }
        return res;
    }
}
