package game;

import java.util.Arrays;
import java.util.Map;

public class HexBoard extends Abstract2DBoard implements Board, Position {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );
//    int dx[] = {0, 0, 1, -1, -1, 1};
//    int dy[] = {1, -1, 0, 0, 1, -1};

    public HexBoard(int n, int k) {
        super(n, n, k,
                new int[] {0, 0, 1, -1, -1, 1},
                new int[] {1, -1, 0, 0, 1, -1}
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
        for (int i = 0; i < n; i++) {
            printSpace( 2 * n - 2 * i + Integer.toString(n).length() - Integer.toString(i + 1).length(), sb);
            sb.append(Integer.toString(i + 1)).append("   ");
            for (int j = 0; j < i; j++) {
                sb.append((CELL_TO_STRING.get(field[i - j - 1][j]))).append("   ");
            }
            sb.append(Integer.toString(i + 1)).append(System.lineSeparator());
        }
        printSpace(3 + Integer.toString(n).length(), sb);
        for (int j = n - 1; n - j <= n; j--) {
            sb.append(CELL_TO_STRING.get(field[j][n - j - 1])).append("   ");
        }
        sb.append(System.lineSeparator());
        for (int i = n + 1; i <= 2 * n; i++) {
            printSpace(2 * i - 2 * n + Integer.toString(n).length() - Integer.toString(i - n).length(), sb);
            sb.append(i - n).append("   ");
            for (int j = n - 1; i - j <= n; j--) {
                sb.append(CELL_TO_STRING.get(field[j][i - j - 1])).append("   ");
            }
            sb.append(i - n).append("   ");
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
