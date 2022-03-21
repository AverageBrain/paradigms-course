package expression;

public class Log extends BinaryOperation {
    public Log(PartOfExpression leftPart, PartOfExpression rightPart) {
        super(leftPart, rightPart);
    }

    @Override
    public String getSign() {
        return "//";
    }

    @Override
    public boolean getAssoc() {
        return false;
    }

    @Override
    public int getPrior() {
        return 3;
    }

    @Override
    public int calculate(int leftValue, int rightValue) {
        int res = 1, base = 0;
        while (res * rightValue <= leftValue) {
            int mulRes = res * rightValue;
            if (res != 0 && mulRes / res != rightValue) {
                break; // overflow by multiply
            }
            res = mulRes;
            base++;
        }
        return base;
    }
}
