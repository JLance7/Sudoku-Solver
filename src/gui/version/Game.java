package gui.version;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game extends JFrame implements ComponentListener, ActionListener {
    //instance variables
    private static final int ROW_SIZE = 9;
    private static final int COL_SIZE = 9;
    private static final int SCREEN_W = 720;
    private static final int SCREEN_H = 720;
    int[][] board = new int[ROW_SIZE][COL_SIZE];
    private static final Color COOL_BLUE =  new Color(0x2F98DE);
    private Cell[][] cells;
    JPanel middle;
    ascii.version.Game controller;

    JButton submitBtn, clearBtn, newPuzzleBtn, solveBtn;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();


    public Game(){
        //setup frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(720, 720);
        this.setTitle("Sudoku Solver");
        ImageIcon icon = new ImageIcon("resources/gameIcon.png");
        this.setIconImage(icon.getImage());
        this.setMinimumSize(new Dimension(600, 600));

        //Add components
        //top text views
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(COOL_BLUE);
        top.setPreferredSize(new Dimension(SCREEN_W, 110));
        addTopComponents(top);
        this.add(top, BorderLayout.NORTH);

        //middle cells
        middle = new JPanel();
        middle.setLayout(new GridLayout(ROW_SIZE, COL_SIZE, 2, 2));
        middle.setBackground(Color.blue);
        addMiddleComponents(middle);
        this.add(middle, BorderLayout.CENTER);

        //logic
        String randomBoard = getPuzzle();
        setRandomBoard(randomBoard);


        //finish setup
        this.addComponentListener(this);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void addTopComponents(JPanel top){
        JLabel label = new JLabel();
        label.setText("Sudoku!");
        label.setFont(new Font("Bell MT", Font.PLAIN, 60));
        label.setForeground(Color.white);
        label.setHorizontalAlignment(JLabel.CENTER);
        top.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(COOL_BLUE);

        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(this);
        submitBtn.setFocusable(false);
        submitBtn.setBackground(new Color(7, 239, 39));
        submitBtn.setFont(new Font("Arial", Font.BOLD, (int)width/42));
        submitBtn.setBorder(BorderFactory.createLineBorder(new Color(6, 142, 24), 1));
        buttonPanel.add(submitBtn);

        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(this);
        clearBtn.setFont(new Font("Arial", Font.BOLD, (int)width/42));
        clearBtn.setBackground(Color.white);
        clearBtn.setFocusable(false);
        clearBtn.setBorder(BorderFactory.createLineBorder(new Color(205, 169, 229), 1));
        buttonPanel.add(clearBtn);

        solveBtn = new JButton("Solve");
        solveBtn.addActionListener(this);
        solveBtn.setFont(new Font("Arial", Font.BOLD, (int)width/42));
        solveBtn.setBackground(new Color(195, 163, 232));
        solveBtn.setFocusable(false);
        solveBtn.setBorder(BorderFactory.createLineBorder(new Color(144, 95, 201), 1));
        buttonPanel.add(solveBtn);

        newPuzzleBtn = new JButton("New Puzzle");
        newPuzzleBtn.addActionListener(this);
        newPuzzleBtn.setFont(new Font("Arial", Font.BOLD, (int)width/42));
        newPuzzleBtn.setBackground(new Color(243, 222, 40));
        newPuzzleBtn.setFocusable(false);
        newPuzzleBtn.setBorder(BorderFactory.createLineBorder(new Color(156, 142, 22), 1));
        buttonPanel.add(newPuzzleBtn);

        top.add(buttonPanel, BorderLayout.CENTER);
    }

    public void addMiddleComponents(JPanel middle){
        //add cells
        int x=0;
        int y=0;
        Cell cell;
        cells = new Cell[ROW_SIZE][COL_SIZE];
        for (int i=0; i<ROW_SIZE; i++){
            for (int j=0; j<COL_SIZE; j++){
                cell = new Cell();
                cells[i][j] = cell;
                middle.add(cell);
            }
        }
    }

    public void resizeText(JButton button){
        double width = this.getWidth();
//        System.out.println(width);
        if (width <= 1000)
            button.setFont(new Font("Arial", Font.BOLD, (int) width / 42));
        else
            button.setFont(new Font("Arial", Font.BOLD, (int) width / 60));
        this.getContentPane().revalidate();
    }

    //takes in random puzzle sequence and sets the cells equal to the filled in numbers
    public void setRandomBoard(String randomBoard){
        int firstEditableRowIndex = 0;
        int firstEditableColIndex = 0;
        boolean getFirstIndex = true;
        int k=0;
        for (int i=0; i<ROW_SIZE; i++){
            for (int j=0; j<COL_SIZE; j++){
                int value = Character.getNumericValue(randomBoard.charAt(k));
                if (value != 0){
                    cells[i][j].getTextField().setText(String.valueOf(value));
                    cells[i][j].getTextField().setEditable(false);
                    cells[i][j].getTextField().setForeground(Color.BLUE);
                }
                else {
                    if (getFirstIndex){
                        firstEditableRowIndex = i;
                        firstEditableColIndex = j;
                        getFirstIndex = false;
                    }
                }
                k++;
            }
        }
        cells[firstEditableRowIndex][firstEditableColIndex].getTextField().requestFocus();
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

    public boolean solveBoard(){
        for (int i=0; i<ROW_SIZE; i++){
            for (int j=0; j<COL_SIZE; j++){
                //if it is empty
                if (cells[i][j].getTextField().getText() == "" || cells[i][j].getTextField().getText() == null){
                    for (int n=1; n<10; n++){
                        if (isValidPlacement(n, i, j)){
                            cells[i][j].getTextField().setText(String.valueOf(n));
                            boolean successfulReturn = solveBoard();
                            if (successfulReturn){
                                return true;
                            }
                            else {
                                //if a next cell can't be solved reset it and try another number
                                cells[i][j].getTextField().setText("");
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
        for (int j=0; j<ROW_SIZE; j++){
            if (cells[row][j].getTextField().getText() == String.valueOf(value))
                return true;
        }
        return false;
    }

    //returns false if number given is in column of board
    public boolean inColumn(int value, int col){
        for (int i=0; i<COL_SIZE; i++){
            if (cells[i][col].getTextField().getText() == String.valueOf(value))
                return true;
        }
        return false;
    }

    //returns false if number given is in 3x3 box of board
    public boolean inBox(int value, int row, int col) {
        //gets upper left box index
        int upperLeftBoxRow = ((int) row/3) * 3;
        int upperLeftBoxCol = ((int) col/3) * 3;

        for (int i = upperLeftBoxRow; i < upperLeftBoxRow + 3; i++) {
            for (int j = upperLeftBoxCol; j < upperLeftBoxCol + 3; j++) {
                if (cells[i][j].getTextField().getText() == String.valueOf(value))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        resizeText(submitBtn);
        resizeText(clearBtn);
        resizeText(newPuzzleBtn);
        resizeText(solveBtn);
    }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentShown(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn){

        }
        if (e.getSource() == clearBtn){
           clear(false);
        }
        if (e.getSource() == solveBtn){
            clear(false);
            solveBoard();
            this.getContentPane().revalidate();
        }
        if (e.getSource() == newPuzzleBtn){
            clear(true);
            String randomBoard = controller.getPuzzle();
            setRandomBoard(randomBoard);
        }
    }

    public void clear(boolean fullClear){
        middle.setVisible(false);
        int firstEditableRowIndex = 0;
        int firstEditableColIndex = 0;
        boolean getFirstEditableIndex = true;

        for (int i=0; i<ROW_SIZE; i++){
            for (int j=0; j<COL_SIZE; j++){
                if (!(cells[i][j].getTextField().isEditable()) && fullClear){
                    cells[i][j].getTextField().setEditable(true);
                    cells[i][j].getTextField().setText("");
                    cells[i][j].getTextField().setForeground(Color.BLACK);
                }
                else if (!(cells[i][j].getTextField().isEditable()) && !fullClear){

                }
                else{
                    if (getFirstEditableIndex){
                        firstEditableRowIndex = i;
                        firstEditableColIndex = j;
                        getFirstEditableIndex = false;
                    }
                    cells[i][j].getTextField().setText("");
                }
            }
        }
        middle.setVisible(true);
        cells[firstEditableRowIndex][firstEditableColIndex].getTextField().requestFocus();
        this.getContentPane().revalidate();
    }
}
