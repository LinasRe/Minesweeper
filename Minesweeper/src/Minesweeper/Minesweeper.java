package Minesweeper;

import java.util.Scanner;
import java.util.Random;

public class Minesweeper {

    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String COLOURRESET = "\u001B[37m";

    private String[][] minesweeperArray;
    private boolean[][] mineArray;
    private String[][] noFogArray;
    private double minePercentage = 0.15;
    private int mineCount = 0;

    public static void main(String[] args) {

        int rows = 10, cols = 10;
        Minesweeper minesweeper = new Minesweeper(rows, cols);
        minesweeper.runGame();

    }

    public Minesweeper(int rows, int cols) {

        Random randomRow = new Random();
        Random randomCol = new Random();

        mineArray = new boolean[rows][cols];
        minesweeperArray = new String[rows][cols];
        noFogArray = new String[rows][cols];

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                mineArray[i][j] = false;
                minesweeperArray[i][j] = "   ";
                noFogArray[i][j] = "   ";
            }
        }

        mineCount = (int)Math.ceil(rows*cols*minePercentage);
        int plantedMines = 0;
        while(plantedMines < mineCount) {
            int row = randomRow.nextInt(rows);
            int col = randomCol.nextInt(cols);
            if(mineArray[row][col] == false) {
                mineArray[row][col] = true;
                plantedMines++;
            }
        }
    }

    public void runGame() {
        System.out.println("----------------Minesweeper----------------");
        System.out.println();

        while(true) {
            printMinesweeperGrid();
           // printNoFogGrid();
            System.out.println("Lauke esančių minų skaičius : " + mineCount + ". ");
            System.out.println("Įveskite komandą: (Įveskite \"help\" norėdami pamatyti galimas komandas)");
            readCommand();
            if(checkIfWon()) {
                revealBombs();
                printMinesweeperGrid();
                System.out.println(" LAIMĖJOTE!");
                System.exit(0);
                break;
            }
        }
    }

    private void printMinesweeperGrid() {
        for(int i = 0; i < minesweeperArray.length; i++) {
            System.out.print(i + " |");
            for(int j = 0; j < minesweeperArray[i].length; j++) {

                if(minesweeperArray[i][j].equals(" X ") || minesweeperArray[i][j].equals(" ! ") )
                    System.out.print(RED + minesweeperArray[i][j] + COLOURRESET);
                if(minesweeperArray[i][j].equals(" 1 "))
                    System.out.print(BLUE + minesweeperArray[i][j] + COLOURRESET);
                if(minesweeperArray[i][j].equals(" 2 "))
                    System.out.print(GREEN + minesweeperArray[i][j] + COLOURRESET);
                if(minesweeperArray[i][j].equals(" 3 "))
                    System.out.print(PURPLE + minesweeperArray[i][j] + COLOURRESET);
                if(minesweeperArray[i][j].equals(" 4 "))
                    System.out.print(YELLOW + minesweeperArray[i][j] + COLOURRESET);
                if(minesweeperArray[i][j].equals("   ") || minesweeperArray[i][j].equals(" 0 "))
                    System.out.print(minesweeperArray[i][j]);
                if(j < minesweeperArray[i].length-1) {
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }
        System.out.print("    ");
        for(int i = 0; i < minesweeperArray[0].length; i++) {
            System.out.print(i + "   ");
        }
        System.out.println();
    }

    public void readCommand() {
        Scanner keyboard = new Scanner(System.in);
        String command = keyboard.next().trim();

        switch(command) {
            case "p":
                int markedRow = keyboard.nextInt();
                int markedCol = keyboard.nextInt();
                minesweeperArray[markedRow][markedCol] = " X ";
                break;
            case "s":
                int guessedRow = keyboard.nextInt();
                int guessedCol = keyboard.nextInt();
                guessCell(guessedRow, guessedCol);
                break;
            case "help":
                System.out.println();
                System.out.println("Galimos komandos:.");
                System.out.println("p eilutė stulpelis (Pvz. \"p 5 6\") - Pažymi vietą kaip galimai turinčią miną.");
                System.out.println("s eilutė stulpelis (Pvz. \"s 3 2\") - Atliekamas spėjimas pasirinktoje vietoje.");
                System.out.println("help - Pateikiamos galimos komandos");
                System.out.println("quit - Išeinama iš žaidimo");
                System.out.println();
                break;
            case "quit":
                System.out.println();
                System.out.println("Iš žaidimo buvo išeita.");
                System.exit(0);
                break;
            default:
                System.out.println("Neatpažinta komanda.");
        }
    }

    private void guessCell(int guessedRow, int guessedCol) {
        if(isInGrid(guessedRow, guessedCol) == true && (minesweeperArray[guessedRow][guessedCol].equals("   ") == true || minesweeperArray[guessedRow][guessedCol].equals(" X ") == true)){
            if(!mineArray[guessedRow][guessedCol]) {
                minesweeperArray[guessedRow][guessedCol] = " " + getNumAdjMines(guessedRow, guessedCol) + " ";
                if(getNumAdjMines(guessedRow, guessedCol) == 0){
                    guessCell(guessedRow-1, guessedCol-1);
                    guessCell(guessedRow-1, guessedCol-0);
                    guessCell(guessedRow-1, guessedCol+1);
                    guessCell(guessedRow+0, guessedCol+1);
                    guessCell(guessedRow+1, guessedCol+1);
                    guessCell(guessedRow+1, guessedCol+0);
                    guessCell(guessedRow+1, guessedCol-1);
                    guessCell(guessedRow+0, guessedCol-1);
                }
            } else {
                revealBombs();
                printMinesweeperGrid();
                System.out.println("Pralaimėjote!");
                System.exit(0);
            }
        }

    }

    ///HELPER FUNKCIJOS///

    private int getNumAdjMines(int row, int col) {
        int numAdjMines = 0;
        for(int i = row-1; i <= row+1; i++) {
            if(!(i >= 0 && i < minesweeperArray.length)) {
                continue;
            }
            for(int j = col-1; j <= col+1; j++) {
                if((i == row && j == col) || (!(j >= 0 && j < minesweeperArray[0].length))) {
                    continue;
                } else {
                    if(mineArray[i][j]) {
                        numAdjMines++;
                    }
                }
            }
        }
        return numAdjMines;
    }

    private boolean isInGrid(int row, int col) {
        return (row >= 0 && row < minesweeperArray.length && col >= 0 && col < minesweeperArray[0].length);
    }

    private boolean checkIfWon() {
        boolean hasWon = true;
        for(int i = 0; i < minesweeperArray.length; i++) {
            for(int j = 0; j < minesweeperArray[0].length; j++) {
                if(mineArray[i][j] == false && minesweeperArray[i][j].equals("   ")) {
                    hasWon = false;
                }
            }
        }
        return hasWon;
    }

    private void revealBombs()
    {
        for(int i = 0; i < minesweeperArray.length; i++) {
            for(int j = 0; j < minesweeperArray[0].length; j++) {
                if(mineArray[i][j])
                    minesweeperArray[i][j] = " ! ";

            }
        }
    }

// TEST FUNKCIJOS///

    private void printNoFogGrid() {
        for(int i = 0; i < noFogArray.length; i++) {
            System.out.print(i + " |");
            for(int j = 0; j < noFogArray[i].length; j++) {
                if(mineArray[i][j]) {
                    System.out.print(" ! ");
                } else {
                    System.out.print(minesweeperArray[i][j]);
                }
                if(j < noFogArray[i].length-1) {
                    System.out.print("|");
                }
            }
            System.out.println("|");
        }
        System.out.print("    ");
        for(int i = 0; i < minesweeperArray[0].length; i++) {
            System.out.print(i + "   ");
        }
        System.out.println();
    }

}