/*
 * NAME: MainFrame
 * AUTHOR: J. Pisani
 * DATE: 8/3/2024
 *
 * DESCRIPTION: Creates the main frame for the program using JFrame. Defaults for the frame are set during construction
 * to set all borders, buttons, etc. All changes to the frame are done through public methods to avoid accidentally
 * making an incorrect change to the frame.
 */

package Frames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame implements ActionListener {
    // Constants
    private static final int FRAME_HEIGHT = 1000;
    private static final int FRAME_WIDTH = 750;

    private JButton exitButton;
    private JButton optionsButton;

    // Create the main frame for the program
    public MainFrame() {
        createExitButton();
        createOptionsButton();

        this.setVisible(true); // Make the frame visible
        this.setLayout(null); // Set to null for now
        this.setTitle("Locals Password Manager"); // Set title for the frame
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // If the main frame is closed, the whole program should close
        this.setBackground(Theme.getMainFrameColor()); // Set the background color of the main frame based on the current theme
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT); // Set default size of the main frame
//        this.setLocationRelativeTo(null); // Start the frame in the center of the display

        // Set Icon for the frame
        ImageIcon imageIcon = new ImageIcon("LocalsIcon.png"); // TODO: get/make an icon
        this.setIconImage(imageIcon.getImage());

        // Add buttons to frame
        this.add(exitButton);
        this.add(optionsButton);
    }

    private void createExitButton() {
        exitButton = new JButton("EXIT"); // TODO: add icon?
        exitButton.setBounds(385,900,355,50);
        exitButton.addActionListener(this);
    }

    private void createOptionsButton() {
        optionsButton = new JButton("OPTIONS"); // TODO: add icon?
        optionsButton.setBounds(10, 900, 355, 50);
        optionsButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exitButton) {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (e.getSource() == optionsButton) {
            OptionsFrame optionsFrame = new OptionsFrame();
        }
    }
}
