import java.io.*;


public class SudokuSolver {

    private static final int taille = 9;
    private int[][] Case;

    public SudokuSolver(String filename) throws IOException {
        this.Case = loadCase(filename);
    }

    private int[][] loadCase(String filename) throws IOException {
        int[][] Case = new int[taille][taille];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String ligne;
            for (int i = 0; i < taille; i++) {
                if ((ligne = br.readLine()) == null || ligne.length() != taille) {
                    throw new IOException("Fomat de fichier incorrect");
                }
                for (int j = 0; j < taille; j++) {
                    char c = ligne.charAt(j);
                    if (c < '0' || c > '9') {
                        throw new IOException("Format de caractère incorrect trouve");
                    }
                    Case[i][j] = c - '0';
                }
            }
        }
        return Case;
    }

    public boolean solve() {
        for (int row = 0; row < taille; row++) {
            for (int col = 0; col < taille; col++) {
                if (Case[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(row, col, num)) {
                            Case[row][col] = num;
                            if (solve()) {
                                return true;
                            }
                            Case[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int row, int col, int num) {
        for (int i = 0; i < taille; i++) {
            if (Case[row][i] == num || Case[i][col] == num) {
                return false;
            }
        }
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Case[boxRowStart + i][boxColStart + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public void display() {
        for (int i = 0; i < taille; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("╠═══════╬═══════╬═══════╣");
            } else if (i == 0) {
                System.out.println("╔═══════╦═══════╦═══════╗");
            } else {
                System.out.println("║       ║       ║       ║");
            }
            for (int j = 0; j < taille; j++) {
                if (j % 3 == 0) {
                    System.out.print("║ ");
                }
                System.out.print(Case[i][j] == 0 ? ". " : Case[i][j] + " ");
            }
            System.out.println("║");
        }
        System.out.println("╚═══════╩═══════╩═══════╝");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Utilise: java SudokuSolver <fichier.txt>");
            return;
        }

        try {
            SudokuSolver solver = new SudokuSolver(args[0]);
            System.out.println("Case initial du sudoku:");
            solver.display();

            if (solver.solve()) {
                System.out.println("\n le Sudoku résolu:");
                solver.display();
            } else {
                System.out.println("\npas de solution possible.");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
