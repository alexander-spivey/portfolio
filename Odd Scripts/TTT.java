import java.util.*;

public class TTT {
    public static String[][] field() {
        final String[][] A = new String[3][3];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                A[i][j] = " ";
            }
        }
        return A;
    }

    public static void print2dMatrix(final String[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                System.out.print("[" + A[i][j] + "]");
            }
            System.out.println("");
        }
    }

    public static void computerMove(final String[][] A) {
        System.out.println("Computer's Move");
        final Random rand = new Random();
        boolean c = false;
        do {
            final int i = rand.nextInt(3);
            final int j = rand.nextInt(3);
            if (A[i][j] == " ") {
                A[i][j] = "X";
                c = true;
            }
        } while (c == false);
        print2dMatrix(A);
    }

    public static boolean pcCheck(final String[][] A) {
        if (A[0][0].equals("X")) {
            if (A[0][1].equals("X")) {
                if (A[0][2].equals("X")) {
                    return true;
                }
            }
            if (A[1][0].equals("X")) {
                if (A[2][0].equals("X")) {
                    return true;
                }
            }
            if (A[1][1].equals("X")) {
                if (A[2][2].equals("X")) {
                    return true;
                }
            }
        }
        if (A[2][2].equals("X")) {
            if (A[2][1].equals("X")) {
                if (A[2][0].equals("X")) {
                    return true;
                }
            }
            if (A[1][2].equals("X")) {
                if (A[0][2].equals("X")) {
                    return true;
                }
            }
        }
        if (A[0][2].equals("X")) {
            if (A[1][1].equals("X")) {
                if (A[2][0].equals("X")) {
                    return true;
                }
            }
        }
        if (A[1][1].equals("X")) {
            if (A[1][0].equals("X")) {
                if (A[1][2].equals("X")) {
                    return true;
                }
            }
            if (A[0][1].equals("X")) {
                if (A[2][1].equals("X")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void yourMove(final String[][] A) {
        System.out.println("Your Move");
        Scanner scan = new Scanner(System.in);
        boolean c = false;
        int i = -1;
        int j = -1;
        do {
            System.out.print("Input y (0-2): ");
            while (!scan.hasNextInt()) {
                System.out.println("Invalid input, numbers only.");
                System.out.print("Input y (0-2): ");
                scan.next();
            }
            i = scan.nextInt();
            System.out.print("Input x (0-2): ");
            while (!scan.hasNextInt()) {
                System.out.println("Invalid input, numbers only.");
                System.out.print("Input x (0-2): ");
                scan.next();
            }
            j = scan.nextInt();
            if (A[i][j] == " ") {
                A[i][j] = "O";
                c = true;
            } else {
                System.out.println("Someone already has tic that tac there!");
            }
        } while (c == false);
        print2dMatrix(A);
    }

    public static boolean playerCheck(final String[][] A) {
        if (A[0][0].equals("O")) {
            if (A[0][1].equals("O")) {
                if (A[0][2].equals("O")) {
                    return true;
                }
            }
            if (A[1][0].equals("O")) {
                if (A[2][0].equals("O")) {
                    return true;
                }
            }
            if (A[1][1].equals("O")) {
                if (A[2][2].equals("O")) {
                    return true;
                }
            }
        }
        if (A[2][2].equals("O")) {
            if (A[2][1].equals("O")) {
                if (A[2][0].equals("O")) {
                    return true;
                }
            }
            if (A[1][2].equals("O")) {
                if (A[0][2].equals("O")) {
                    return true;
                }
            }
        }
        if (A[0][2].equals("O")) {
            if (A[1][1].equals("O")) {
                if (A[2][0].equals("O")) {
                    return true;
                }
            }
        }
        if (A[1][1].equals("O")) {
            if (A[1][0].equals("O")) {
                if (A[1][2].equals("O")) {
                    return true;
                }
            }
            if (A[0][1].equals("O")) {
                if (A[2][1].equals("O")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean empty(final String[][] A, final int i, final int j) {
        if (A[i][j] == " ") {
            return true;
        } else {
            return false;
        }
    }

    public static void pcAi(final String[][] A) {
        System.out.println("Computer's Move");
        final Random rand = new Random();
        boolean c = false;
        if (A[2][1].equals("O")) // bottom win
        {
            if (A[2][0].equals("O")) {
                if (empty(A, 2, 2)) {
                    A[2][2] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
            if (A[2][2].equals("O")) {
                if (empty(A, 2, 0)) {
                    A[2][0] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }
        if (A[2][0].equals("O")) {
            if (A[2][2].equals("O")) {
                if (empty(A, 2, 1)) {
                    A[2][1] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }

        if (A[1][0].equals("O")) // left side win
        {
            if (A[2][0].equals("O")) {
                if (empty(A, 0, 0)) {
                    A[0][0] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
            if (A[0][0].equals("O")) {
                if (empty(A, 2, 0)) {
                    A[2][0] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }
        if (A[0][0].equals("O")) {
            if (A[2][0].equals("O")) {
                if (empty(A, 1, 0)) {
                    A[1][0] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }

        if (A[1][2].equals("O")) // right side win
        {
            if (A[2][2].equals("O")) {
                if (empty(A, 0, 2)) {
                    A[0][2] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
            if (A[0][2].equals("O")) {
                if (empty(A, 2, 2)) {
                    A[2][2] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }
        if (A[0][2].equals("O")) {
            if (A[2][2].equals("O")) {
                if (empty(A, 1, 2)) {
                    A[1][2] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }

        if (A[1][1].equals("O")) // mid side win
        {
            if (A[2][1].equals("O")) {
                if (empty(A, 0, 1)) {
                    A[0][1] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
            if (A[0][1].equals("O")) {
                if (empty(A, 2, 1)) {
                    A[2][2] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }
        if (A[0][1].equals("O")) {
            if (A[2][1].equals("O")) {
                if (empty(A, 1, 1)) {
                    A[1][1] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }

        if (A[0][1].equals("O")) // top side win
        {
            if (A[0][0].equals("O")) {
                if (empty(A, 0, 2)) {
                    A[0][2] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
            if (A[0][2].equals("O")) {
                if (empty(A, 0, 0)) {
                    A[0][0] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }
        if (A[0][0].equals("O")) {
            if (A[0][2].equals("O")) {
                if (empty(A, 0, 1)) {
                    A[0][1] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }

        if (A[1][1].equals("O")) // true mid1 win
        {
            if (A[1][0].equals("O")) {
                if (empty(A, 1, 2)) {
                    A[1][2] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
            if (A[1][2].equals("O")) {
                if (empty(A, 1, 0)) {
                    A[1][0] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }
        if (A[1][0].equals("O")) {
            if (A[1][2].equals("O")) {
                if (empty(A, 1, 1)) {
                    A[1][1] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }

        if (A[1][1].equals("O")) // right diagonal win
        {
            if (A[0][2].equals("O")) {
                if (empty(A, 2, 0)) {
                    A[2][0] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
            if (A[2][0].equals("O")) {
                if (empty(A, 0, 2)) {
                    A[0][2] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }
        if (A[0][2].equals("O")) {
            if (A[2][0].equals("O")) {
                if (empty(A, 1, 1)) {
                    A[1][1] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }

        if (A[1][1].equals("O")) // left diagonal win
        {
            if (A[0][0].equals("O")) {
                if (empty(A, 2, 2)) {
                    A[2][2] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
            if (A[2][2].equals("O")) {
                if (empty(A, 0, 0)) {
                    A[0][0] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }
        if (A[0][0].equals("O")) {
            if (A[2][2].equals("O")) {
                if (empty(A, 1, 1)) {
                    A[1][1] = "X";
                    print2dMatrix(A);
                    return;
                }
            }
        }

        do {
            final int i = rand.nextInt(3);
            final int j = rand.nextInt(3);
            if (A[i][j] == " ") {
                A[i][j] = "X";
                c = true;
            }
        } while (c == false);
        print2dMatrix(A);
    }

    public static void main(final String[] arg) {
        final String[][] field = field();
        print2dMatrix(field);
        System.out.println("Welcome to tic tac toe! Let's play...");
        int ct = 0;
        while (true) {
            // computerMove(field); random number shit
            pcAi(field);
            if (pcCheck(field)) {
                System.out.println("Winner is PC");
                break;
            }
            if (ct == 4) {
                System.out.println("No one wins :(");
                break;
            }
            yourMove(field);
            if (playerCheck(field)) {
                System.out.println("Winner is Player");
                break;
            }
            ct++;
        }
    }
}