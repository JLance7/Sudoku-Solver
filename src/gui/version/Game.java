package gui.version;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class Game extends JFrame implements ComponentListener, ActionListener {
    //instance variables
    private static final int ROW_SIZE = 9;
    private static final int COL_SIZE = 9;
    private static final int SCREEN_W = 720;
    private static final int SCREEN_H = 720;
    private static final Color COOL_BLUE =  new Color(0x2F98DE);
    private Cell[][] cells;
    private JPanel middle;


    JButton submitBtn, clearBtn, newPuzzleBtn, solveBtn;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();


    public Game(){
        System.setProperty("sun.awt.noerasebackground", "true");
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
        label.setFont(new Font("Courier New", Font.PLAIN, 60));
        label.setForeground(Color.white);
        label.setHorizontalAlignment(JLabel.CENTER);
        top.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBackground(COOL_BLUE);

        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(this);
        submitBtn.setFocusable(false);
        submitBtn.setBackground(new Color(7, 239, 39));
        submitBtn.setFont(new Font("Courier New", Font.BOLD, (int)width/42));
        submitBtn.setBorder(BorderFactory.createLineBorder(new Color(6, 142, 24), 1));
        buttonPanel.add(submitBtn);

        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(this);
        clearBtn.setFont(new Font("Courier New", Font.BOLD, (int)width/42));
        clearBtn.setBackground(Color.white);
        clearBtn.setFocusable(false);
        clearBtn.setBorder(BorderFactory.createLineBorder(new Color(205, 169, 229), 1));
        buttonPanel.add(clearBtn);

        solveBtn = new JButton("Solve");
        solveBtn.addActionListener(this);
        solveBtn.setFont(new Font("Courier New", Font.BOLD, (int)width/42));
        solveBtn.setBackground(new Color(195, 163, 232));
        solveBtn.setFocusable(false);
        solveBtn.setBorder(BorderFactory.createLineBorder(new Color(144, 95, 201), 1));
        buttonPanel.add(solveBtn);

        newPuzzleBtn = new JButton("New Puzzle");
        newPuzzleBtn.addActionListener(this);
        newPuzzleBtn.setFont(new Font("Courier New", Font.BOLD, (int)width/42));
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

                cell.getTextField().addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        repaint();
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        repaint();
                    }
                });
                cell.getTextField().addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        validate();
                        repaint();
//                        if (e.getKeyChar() == KeyEvent.VK_SPACE)
                    }
                    @Override
                    public void keyPressed(KeyEvent e) {
                        validate();
                        repaint();
                    }
                    @Override
                    public void keyReleased(KeyEvent e) {
                        validate();
                        repaint();
                    }
                });
            }
        }
    }

    //get random puzzle string from text file
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

    //backtracking through recursion solution
    public boolean solveBoard(){
        for (int i=0; i<ROW_SIZE; i++){
            for (int j=0; j<COL_SIZE; j++){
                //if it is empty
                if (cells[i][j].getTextField().getText().equals("") || cells[i][j].getTextField().getText().isEmpty()){
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
            if (cells[row][j].getTextField().getText().equals(String.valueOf(value))){
                return true;
            }
        }
        return false;
    }

    //returns false if number given is in column of board
    public boolean inColumn(int value, int col){
        for (int i=0; i<COL_SIZE; i++){
            if (cells[i][col].getTextField().getText().equals(String.valueOf(value))){
                return true;
            }
        }

        return false;
    }

    //returns false if number given is in 3x3 box of board
    public boolean inBox(int value, int row, int col) {
        //gets upper left box index
        int upperLeftBoxRow = ( (int) row/3) * 3;
        int upperLeftBoxCol = ( (int) col/3) * 3;

        for (int i = upperLeftBoxRow; i < upperLeftBoxRow + 3; i++) {
            for (int j = upperLeftBoxCol; j < upperLeftBoxCol + 3; j++) {
                if (cells[i][j].getTextField().getText().equals(String.valueOf(value))){
                    return true;
                }
            }
        }
        return false;
    }

    public void resizeText(JButton button){
        double width = this.getWidth();
//        System.out.println(width);
        if (width <= 1000)
            button.setFont(new Font("Courier New", Font.BOLD, (int) width / 42));
        else
            button.setFont(new Font("Courier New", Font.BOLD, (int) width / 60));
//        this.getContentPane().revalidate();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        resizeText(submitBtn);
        resizeText(clearBtn);
        resizeText(newPuzzleBtn);
        resizeText(solveBtn);
        //resize cell text
        for (int i=0; i< ROW_SIZE; i++){
            for (int j=0; j< COL_SIZE; j++){
                Cell cell = cells[i][j];
                int width = cell.getTextField().getWidth();
                cell.getTextField().setFont(new Font("Roboto", Font.PLAIN, width/2));
            }
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentShown(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }

    //buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        int frameWidth = this.getWidth();
        int frameHeight = this.getHeight();
        if (e.getSource() == submitBtn){
            removeListeners();
            boolean win = checkWin();
            if (win){
                JOptionPane.showMessageDialog(this, "You win!", "Win", JOptionPane.PLAIN_MESSAGE);
                newPuzzleBtn.doClick();
            }
            else {
                JOptionPane.showMessageDialog(this, "Incorrect Solution", "Incorrect", JOptionPane.PLAIN_MESSAGE);
            }
            addListeners();
        }
        if (e.getSource() == clearBtn){
            removeListeners();
            clear(false);
            addListeners();
        }
        if (e.getSource() == solveBtn){
            removeListeners();
            clear(false);
            solveBoard();
            for (int i=0; i<ROW_SIZE; i++){
                for (int j=0; j<COL_SIZE; j++){
                    cells[i][j].getTextField().setEditable(false);
                }
            }
            addListeners();
            this.setSize(frameWidth + 1, frameHeight + 1);
            this.setSize(frameWidth - 1, frameHeight - 1);
        }
        if (e.getSource() == newPuzzleBtn){
            removeListeners();
            clear(true);
            for (int i=0; i<ROW_SIZE; i++){
                for (int j=0; j<COL_SIZE; j++){
                    cells[i][j].getTextField().setEditable(true);
                }
            }
            String randomBoard = getPuzzle();
            setRandomBoard(randomBoard);
            addListeners();
        }

        this.setSize(frameWidth + 1, frameHeight + 1);
        this.setSize(frameWidth - 1, frameHeight - 1);
        validate();
        repaint();
    }

    public void removeListeners(){
        submitBtn.removeActionListener(this);
        clearBtn.removeActionListener(this);
        solveBtn.removeActionListener(this);
        newPuzzleBtn.removeActionListener(this);
    }

    public void addListeners(){
        submitBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        solveBtn.addActionListener(this);
        newPuzzleBtn.addActionListener(this);
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g2);

        int width = middle.getWidth();
        int height = middle.getHeight();
        int screenHeight = (int)screenSize.getHeight();
        int screenWidth = (int)screenSize.getWidth();
        //System.out.println(width);
        g2.setStroke(new BasicStroke(5));

        //vertical lines
        g2.drawLine(width/3 + 8, 143, width/3 + 8, screenHeight);
        g2.drawLine(width/3 * 2 + 8, 143, width/3 * 2 + 8, screenHeight);

        //horizontal lines
        g2.drawLine(0, height/3 - 8 + 148, screenWidth, height/3 - 8 + 148);
        g2.drawLine(0, (height/3) * 2 -8 + 148, screenWidth, (height/3) * 2 -8 + 148);
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

    public boolean checkWin(){
        for (int i=0; i<ROW_SIZE; i++){
            for (int j=0; j<COL_SIZE; j++){
                if (cells[i][j].getTextField().getText().equals(""))
                    return false;
                int value = Integer.parseInt(cells[i][j].getTextField().getText());
                if (isValidPlacementSubmit(value, i, j)){
                    continue;
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidPlacementSubmit(int value, int row, int col){
        //check row only has one of the same value
        int count = 0;
        for (int j=0; j<col; j++){
            if (cells[row][j].getTextField().getText().equals(String.valueOf(value))){
                count++;
            }
            if (count == 2)
                return false;
        }

        count = 0;
        //check col only has one of the same value
        for (int i=0; i<col; i++){
            if (cells[i][col].getTextField().getText().equals(String.valueOf(value))){
                count++;
            }
            if (count == 2)
                return false;
        }

        count = 0;
        //check box only has one of the same value
        //gets upper left box index
        int upperLeftBoxRow = ( (int) row/3) * 3;
        int upperLeftBoxCol = ( (int) col/3) * 3;

        for (int i = upperLeftBoxRow; i < upperLeftBoxRow + 3; i++) {
            for (int j = upperLeftBoxCol; j < upperLeftBoxCol + 3; j++) {
                if (cells[i][j].getTextField().getText().equals(String.valueOf(value))){
                    count++;
                }
                if (count == 2)
                    return false;
            }
        }

        return true;
    }
}
