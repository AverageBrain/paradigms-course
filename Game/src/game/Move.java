package game;

public class Move {
    private final int row;
    private final int col;
    private final Cell value;
    private int type;

    public Move(int row, int col, Cell value) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.type = 0;
    }

    public int getType() {
        return type;
    }

    public Move changeType(int type) {
        this.type = type;
        return this;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Cell getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Move(%s, %d, %d)", value, row + 1, col + 1);
    }
}
