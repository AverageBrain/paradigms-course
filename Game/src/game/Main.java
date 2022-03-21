package game;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Choose game please: 1 - NMKGame, 2 - HexGame");
        int game = 0;
        while (true) {
            game = in.nextInt();
            if (game != 1 && game != 2) {
                System.out.println("Incorrect input");
            } else {
                break;
            }
        }
        System.out.println("Enter game parameters: ");
        final int n, m, k;
        if (game == 1) {
            System.out.print("n m k:");
            n = in.nextInt();
            m = in.nextInt();
        } else {
            System.out.print("n k:");
            n = in.nextInt();
            m = 0;
        }
        k = in.nextInt();
        final int result = new TwoPlayerGame(
                (game == 1? new TicTacToeBoard(n, m, k): new HexBoard(n, k)),
                new HumanPlayer(in),
                new HumanPlayer(in)
        ).play(true);
        switch (result) {
            case 1 -> System.out.println("First player won");
            case 2 -> System.out.println("Second player won");
            case 0 -> System.out.println("Draw");
            default -> throw new AssertionError("Unknown result " + result);
        }
    }
}
