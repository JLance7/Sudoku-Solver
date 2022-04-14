package ascii.version;

import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

//ascii version to test logic, array must be hardcoded to test
public class Game {
    private static final int GRID_SIZE = 9;
    private static int[][] board = new int[9][9];

    //main loop to print an ascii version of game
    public static void main(String[] args) {
        Game game = new Game();
        String randomBoard = game.getPuzzle();
        game.populateBoard(randomBoard);

        System.out.println("The starting board:");
        game.printBoard();

        if (game.solveBoard()){
            System.out.println("\n\nThe solved board:");
            game.printBoard();
        }
        else {
            System.out.println("\nThe board can not be solved.");
        }
    }

    public String getPuzzle(){
        //unsolved sudoku boards text file taken from http://www.kokolikoko.com/sudoku/
        int numOfLines = 10000;
        String randomBoard = "";

        try {
            File file = new File("resources/boards.txt");
            Scanner reader = new Scanner(file);
            Random random = new Random();
            int randomLine = random.nextInt((numOfLines - 1) + 1) + 1;
            //System.out.println("Random Line is: " + randomLine);
            int currentLine = 0;

            while (reader.hasNextLine()){
                currentLine++;
                if (currentLine == randomLine){
                    randomBoard = reader.nextLine();
                    break;
                }
                reader.nextLine();
            }
        } catch (Exception e){
            System.out.println("Error reading file");
        }

        //System.out.println(randomBoard);
        return randomBoard;
    }

    public void populateBoard(String randomBoard){
        int stringIndex = 0;
        for (int i=0; i<GRID_SIZE; i++){
            for (int j = 0; j < GRID_SIZE; j++) {
                board[i][j] = Character.getNumericValue(randomBoard.charAt(stringIndex));
                stringIndex++;
            }
        }

    }

    public void printBoard(){
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                int value = board[i][j];
                if (j+1 == 3 || j+1 == 6){
                    System.out.print(value + "|");
                }
                else {
                    System.out.print(value);
                }
            }
            System.out.println();
            if (i+1 == 3 || i+1 == 6)
                System.out.println("---|---|---");
        }
    }

    public boolean solveBoard(){
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                //if it is empty
                if (board[i][j] == 0){
                    for (int n=1; n<10; n++){
                        if (isValidPlacement(n, i, j)){
                            board[i][j] = n;
                            boolean successfulReturn = solveBoard();
                            if (successfulReturn){
                                return true;
                            }
                            else {
                                //if a next cell can't be solved reset it and try another number
                                board[i][j] = 0;
                            }
                        }

                    }
                    //if the spot needs to be filled and none of the values work return false to indicate error
                    return false;
                }
            }
        }
        //if all cells are filled it is solved
        return true;
    }

    public boolean isValidPlacement(int value, int row, int col){
        if ( !inRow(value, row) && !inColumn(value, col) && !inBox(value, row, col) ){
            return true;
        }
        return false;
    }

    //returns false if number given is in row of board
    public boolean inRow(int value, int row){
        for (int j=0; j<GRID_SIZE; j++){
            if (board[row][j] == value)
                return true;
        }
        return false;
    }

    //returns false if number given is in column of board
    public boolean inColumn(int value, int col){
        for (int i=0; i<GRID_SIZE; i++){
            if (board[i][col] == value)
                return true;
        }
        return false;
    }

    //returns false if number given is in 3x3 box of board
    public boolean inBox(int value, int row, int col){
        //gets upper left box index
        int[] boxIndexes = getBoxIndexes(row, col);
        int upperLeftBoxRow = boxIndexes[0];
        int upperLeftBoxCol = boxIndexes[1];

        for (int i=upperLeftBoxRow; i<upperLeftBoxRow + 3; i++){
            for (int j=upperLeftBoxCol; j<upperLeftBoxCol + 3; j++){
                if (board[i][j] == value)
                    return true;
            }
        }
        return false;
    }

    //for testing correct indexes for checkBox (returns row index and col index)
    public int[] getBoxIndexes(int row, int col){
        int upperLeftBoxRow = ((int) row/3) * 3;
        int upperLeftBoxCol = ((int) col/3) * 3;
        int[] indexes = {upperLeftBoxRow, upperLeftBoxCol};
        return indexes;
    }

    public void clearBoard(){
        for (int i=0; i<GRID_SIZE; i++){
            for (int j=0; j<GRID_SIZE; j++){
                board[i][j] = 0;
            }
        }
    }
}
