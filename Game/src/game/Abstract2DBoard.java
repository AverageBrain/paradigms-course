package game;

import java.util.Arrays;
import java.util.Map;

public abstract class Abstract2DBoard implements Board, Position {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );
    protected int[] dx;
    protected int[] dy;
    protected final Cell[][] field;
    protected Cell turn;
    protected ProxyPosition playerBoard;
    protected final int n, m, k;
    private int cntEmptyCell;

    public Abstract2DBoard(int n, int m, int k, int[] dx, int[] dy) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.dx = dx;
        this.dy = dy;
        this.cntEmptyCell = n * m;
        field = new Cell[n][m];
        for (Cell[] row : field) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    @Override
    public Cell getTurn() {
        return turn;
    }

    @Override
    public Position getPosition() {
        return new ProxyPosition(this);
    }

    @Override
    public GameResult makeMove(Move move) {
        if (move.getType() == 0) {
            if (!isValid(move)) {
                return GameResult.LOOSE;
            }
            cntEmptyCell--;

            field[move.getRow()][move.getCol()] = move.getValue();
            if (checkWin(move)) {
                return GameResult.WIN;
            }

            if (checkDraw()) {
                return GameResult.DRAW;
            }

            turn = turn == Cell.X ? Cell.O : Cell.X;
            return GameResult.UNKNOWN;
        } else if (move.getType() == 1) {
            return GameResult.LOOSE;
        } else if (move.getType() == 2) {
            return GameResult.OFFERDRAW;
        } else {
            return GameResult.LOOSE;
        }
    }

    private boolean checkDraw() {
        return (cntEmptyCell == 0);
    }

    private boolean checkWin(Move move) {
        int curX = move.getRow();
        int curY = move.getCol();

        for (int i = 0; i < dx.length; i++) {
            int count = 0;
            for (int j = 1; j <= k - 1; j++) {
                if (0 <= curX + j * dx[i] && curX + j * dx[i] < n
                        && 0 <= curY + j * dy[i] && curY + j * dy[i] < n
                        && field[curX + j * dx[i]][curY + j * dy[i]] == turn) {
                    count++;
                }
            }
            if (count == k - 1) {
                return true;
            }
        }

        return false;
    }

    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < n
                && 0 <= move.getCol() && move.getCol() < m
                && field[move.getRow()][move.getCol()] == Cell.E
                && turn == move.getValue();
    }

    @Override
    public Cell getCell(int row, int column) {
        return field[row][column];
    }
}
