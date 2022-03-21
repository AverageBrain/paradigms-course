package expression;

public class T0 extends UnaryOperation {
    public T0(PartOfExpression operand) {
        super(operand);
    }

    @Override
    public String getSign() {
        return "t0";
    }

    @Override
    public int calculate(int value) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if (((value >> i)& 1) == 0) {
                res++;
            } else {
                break;
            }
        }
        return res;
    }
}
