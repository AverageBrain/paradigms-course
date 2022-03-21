package game;

public class TwoPlayerGame {
    private final Board board;
    private final Player player1;
    private final Player player2;

    public TwoPlayerGame(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int play(boolean log) {
        while (true) {
            int result1 = makeMove(player1, 1, log);
            if (result1 == -2) {
                if (player2.offerDraw(board.getPosition())) {
                    return 0;
                } else {
                    result1 = makeMove(player1, 1, log);
                }
            }
            if (result1 >= 0)  {
                return result1;
            }

            int result2 = makeMove(player2, 2, log);
            if (result2 == -2) {
                if (player1.offerDraw(board.getPosition())) {
                    return 0;
                } else {
                    result2 = makeMove(player2, 1, log);
                }
            }
            if (result2 >= 0)  {
                return result2;
            }
        }
    }

    private int makeMove(Player player, int no, boolean log) {
        final Move move = player.makeMove(board.getPosition());
        final GameResult result = board.makeMove(move);
        if (log) {
            System.out.println();
            System.out.println("Player: " + no);
            System.out.println(move);
            System.out.println(board);
            System.out.println("Result: " + result);
        }
        switch (result) {
            case OFFERDRAW:
                return -2;
            case WIN:
                return no;
            case LOOSE:
                return 3 - no;
            case DRAW:
                return 0;
            case UNKNOWN:
                return -1;
            default:
                throw new AssertionError("Unknown makeMove result " + result);
        }
    }
}
