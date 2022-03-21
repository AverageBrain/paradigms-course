package expression;

public class Const implements PartOfExpression {
    private final Integer cnst;

    @Override
    public int getPrior() {
        return 5;
    }

    public Const(int cnst) {
        this.cnst = cnst;
    }

    public int getNumber() {
        return cnst;
    }

    @Override
    public int evaluate(int value) {
        return cnst;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return cnst;
    }

    @Override
    public String toString() {
        return cnst.toString();
    }

    @Override
    public String toMiniString() {
        return cnst.toString();
    }

    @Override
    public boolean equals(Object expr) {
        if (this == expr) return true;
        if (expr == null || getClass() != expr.getClass()) return false;
        return (this.getNumber() == ((Const) expr).getNumber());
    }

    @Override
    public int hashCode() {
        return cnst.hashCode();
    }
}
