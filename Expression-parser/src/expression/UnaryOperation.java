package expression;

public abstract class UnaryOperation implements PartOfExpression {
    private final PartOfExpression operand;

    protected UnaryOperation(PartOfExpression operand) {
        this.operand = operand;
    }

    abstract public String getSign();

    public int getPrior() {
        return 4;
    }

    abstract public int calculate(int value);

    public PartOfExpression getOperand() {
        return operand;
    }

    @Override
    public int evaluate(int value) {
        return calculate(operand.evaluate(value));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(operand.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return getSign() + '(' + operand.toString() + ')';
    }

    private String withBrackets(PartOfExpression expr) {
        return '(' + operand.toMiniString() + ')';
    }

    private boolean isConstOrVariable(PartOfExpression expr) {
        return (expr instanceof Const || expr instanceof Variable);
    }

    //rewrite toMiniString with associativity and priority
    @Override
    public String toMiniString() {
        if (isConstOrVariable(operand) || operand instanceof UnaryOperation) {
            return getSign() + ' ' + operand.toMiniString();
        } else {
            return getSign() + withBrackets(operand);
        }
    }

    @Override
    public boolean equals(Object expr) {
        if (expr == this) return true;
        if (expr == null || getClass() != expr.getClass()) {
            return false;
        }
        UnaryOperation cur = (UnaryOperation) expr;
        return this.getOperand().equals(cur.getOperand());
    }

    @Override
    public int hashCode() {
        return (this.hashCode() * 1049 + this.getClass().hashCode());
    }
}
