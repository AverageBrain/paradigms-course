package expression;

public class L0 extends UnaryOperation {
    public L0(PartOfExpression operand) {
        super(operand);
    }

    @Override
    public String getSign() {
        return "l0";
    }

    @Override
    public int calculate(int value) {
        int res = 0;
        for (int i = 31; i >= 0; i--) {
            if (((value >> i)& 1) == 0) {
                res++;
            } else {
                break;
            }
        }
        return res;
    }
}
