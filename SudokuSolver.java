import java.io.*;

// Classe principale pour résoudre le Sudoku
public class SudokuSolver {

    // Taille de la grille Sudoku (9x9)
    private static final int taille = 9;
    // Tableau 2D pour stocker la grille Sudoku
    private int[][] Case;

    // Constructeur qui initialise la grille à partir d'un fichier
    public SudokuSolver(String filename) throws IOException {
        this.Case = loadCase(filename);
    }

    // Méthode pour charger la grille Sudoku à partir d'un fichier
    private int[][] loadCase(String filename) throws IOException {
        int[][] Case = new int[taille][taille];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String ligne;
            for (int i = 0; i < taille; i++) {
                // Lire une ligne du fichier
                if ((ligne = br.readLine()) == null || ligne.length() != taille) {
                    throw new IOException("Format de fichier incorrect");
                }
                for (int j = 0; j < taille; j++) {
                    char c = ligne.charAt(j);
                    // Vérifier si le caractère est un chiffre entre '0' et '9'
                    if (c < '0' || c > '9') {
                        throw new IOException("Format de caractère incorrect trouvé");
                    }
                    // Convertir le caractère en entier et le stocker dans le tableau
                    Case[i][j] = c - '0';
                }
            }
        }
        return Case;
    }

    // Méthode pour résoudre le Sudoku
    public boolean solve() {
        for (int row = 0; row < taille; row++) {
            for (int col = 0; col < taille; col++) {
                // Si la case est vide (valeur 0)
                if (Case[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        // Vérifier si le nombre peut être placé dans la case
                        if (estValide(row, col, num)) {
                            Case[row][col] = num;
                            // Appel récursif pour continuer à résoudre
                            if (solve()) {
                                return true;
                            }
                            // Si la solution n'est pas trouvée, remettre la case à 0
                            Case[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // Méthode pour vérifier si un nombre peut être placé dans une case
    private boolean estValide(int row, int col, int num) {
        for (int i = 0; i < taille; i++) {
            // Vérifier la ligne et la colonne
            if (Case[row][i] == num || Case[i][col] == num) {
                return false;
            }
        }
        // Calculer les indices de début de la sous-grille 3x3
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Vérifier la sous-grille 3x3
                if (Case[boxRowStart + i][boxColStart + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    // Méthode pour afficher la grille Sudoku
    public void display() {
        for (int i = 0; i < taille; i++) {
            // Afficher les lignes de séparation
            if (i % 3 == 0 && i != 0) {
                System.out.println("╠═══════╬═══════╬═══════╣");
            } else if (i == 0) {
                System.out.println("╔═══════╦═══════╦═══════╗");
            } else {
                System.out.println("║       ║       ║       ║");
            }
            for (int j = 0; j < taille; j++) {
                // Afficher les séparateurs de colonnes
                if (j % 3 == 0) {
                    System.out.print("║ ");
                }
                // Afficher les valeurs de la grille ou un point pour les cases vides
                System.out.print(Case[i][j] == 0 ? ". " : Case[i][j] + " ");
            }
            System.out.println("║");
        }
        // Afficher la ligne inférieure de la grille
        System.out.println("╚═══════╩═══════╩═══════╝");
    }

    // Méthode principale pour exécuter le programme
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
            System.err.println("Erreur: " + e.getMessage());
        }
    }
}
