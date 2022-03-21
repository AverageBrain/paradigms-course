package game;

import java.util.Arrays;
import java.util.Map;

public class TicTacToeBoard extends Abstract2DBoard implements Board, Position {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );

    public TicTacToeBoard(int n, int m, int k) {
        super(n, m, k,
                new int[] {0, 0, 1, -1, -1, 1, -1, 1},
                new int[] {-1, 1, 0, 0, -1, 1, 1, -1}
        );
    }

    private void printSpace(int cnt, StringBuilder sb) {
        for (int i = 0; i < cnt; i++) {
            sb.append(' ');
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        int sz = Integer.toString(Math.max(n, m)).length() + 2;
        printSpace(sz, sb);
        for (int i = 1; i <= m; i++) {
            int k = Integer.toString(i).length();
            printSpace((sz - k + (k + 1)% 2)/ 2, sb);
            sb.append(Integer.toString(i));
            printSpace((sz - k - (k + 1)% 2)/ 2, sb);
        }
        sb.append(System.lineSeparator());
        for (int r = 0; r < n; r++) {
            sb.append(r + 1);
            int suf = sz - Integer.toString(r + 1).length();
            printSpace(suf, sb);
            for (Cell cell : field[r]) {
                printSpace((sz - 1)/ 2, sb);
                sb.append(CELL_TO_STRING.get(cell));
                printSpace((sz - 1)/ 2, sb);
            }
            sb.append(System.lineSeparator());
        }
        sb.setLength(sb.length() - System.lineSeparator().length());
        return sb.toString();
    }

}
