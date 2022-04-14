package gui.version;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Cell extends JPanel {
    private static final int CELL_W = 80;
    private static final int CELL_H = 80;
    JTextField textField;

    Cell(){
        this.setLayout(new GridLayout());
        this.setPreferredSize(new Dimension(CELL_W, CELL_H));
        this.setBackground(Color.pink);
        //this.setBounds(0, 0, 200, 100);
        //this.setBorder(BorderFactory.createLineBorder(Color.black));

        //add text field
        textField = new JTextField(1);
        textField.setPreferredSize(new Dimension(CELL_W, CELL_H));
        textField.setBackground(Color.white);
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setHorizontalAlignment(JTextField.CENTER);
        int width = (int) (CELL_W/1.7);
        textField.setFont(new Font("Roboto", Font.PLAIN, width));
        this.add(textField);
        textField.setDocument(new JTextFieldLimit(1));
    }

    public JTextField getTextField(){
        return textField;
    }

}
