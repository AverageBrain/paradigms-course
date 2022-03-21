package game;

import java.sql.SQLOutput;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner in;
    private boolean isPrevDraw;

    public HumanPlayer(Scanner in) {
        this.in = in;
        this.isPrevDraw = false;
    }

    private Move scanMove(Position position) {
        if (in.hasNextInt()) {
            int row = in.nextInt();
            int col = in.nextInt();
            Move move = new Move(row- 1, col - 1, position.getTurn());
            return move;
        } else {
            String text = in.next();
            if (text.equals("GiveUp")) {
                return new Move(-1, -1, position.getTurn()).changeType(1);
            } else if (text.equals("Draw") && !isPrevDraw) {
                isPrevDraw = true;
                return new Move(-1, -1, position.getTurn()).changeType(2);
            } else {

                return new Move(-1, -1, position.getTurn());
            }
        }
    }

    @Override
    public Move makeMove(Position position) {
        int cnt = 0;
        while (true) {
            System.out.println();
            System.out.println("Current position");
            System.out.println(position);
            System.out.println("Enter 'GiveUp' to give up.");
            if (!isPrevDraw) {
                System.out.println("Enter 'Draw' to offer a draw.");
            } else {
                System.out.println("Your opponent refused a draw.");
            }
            System.out.println();
            if (cnt > 0) {
                System.out.println("Your move was incorrect. Be careful!");
            }
            System.out.println("Enter you move for " + position.getTurn());
            Move curMove = scanMove(position);
            if (curMove.getType() != 0 || position.isValid(curMove)) {
                if (curMove.getType() != 2) {
                    isPrevDraw = false;
                }
                return curMove;
            }
            cnt++;
        }
    }

    public boolean offerDraw(Position position) {
        System.out.println("Your opponent requests DRAW. Do you want to agree?");
        System.out.println("Y - yes, N - no");
        String text = in.next();
        return text.equals("Y");
    }
}
