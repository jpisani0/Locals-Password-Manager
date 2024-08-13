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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame implements ActionListener {
    // Constants
    private static final int FRAME_HEIGHT = 1000;
    private static final int FRAME_WIDTH = 750;

    // Panels for the frame
    private JPanel topPanel;
    private JPanel mainPanel;
    private JPanel bottomPanel;

    // Buttons for the frame
    private JButton exitButton;
    private JButton optionsButton;

    // Create the main frame for the program
    public MainFrame(String username) {
        createExitButton();
        createOptionsButton();
        createTopPanel();
        createMainPanel();
        createBottomPanel();
        createMainFrame(username);
    }

    /* ------- Methods for constructing the buttons of the frame -------- */
    private void createExitButton() {
        exitButton = new JButton("EXIT"); // TODO: add icon?
        exitButton.addActionListener(this); // Add listener for loginButton pressed
        exitButton.setFocusable(false); // Make loginButton not focusable
        exitButton.setForeground(Color.WHITE); // Set text color
        exitButton.setBackground(Color.DARK_GRAY); // Set background of loginButton color
        exitButton.setHorizontalAlignment(JButton.CENTER); // Set text to center horizontally
        exitButton.setVerticalAlignment(JButton.CENTER); // Set text to center vertically
        exitButton.setFont(new Font("Comic Sans", Font.BOLD, 25)); // Set the font, special text option, and size
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
//        exitButton.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.BLUE));
    }

    private void createOptionsButton() {
        optionsButton = new JButton("OPTIONS"); // TODO: add icon?
        optionsButton.addActionListener(this);
        optionsButton.setFocusable(false);
        optionsButton.setForeground(Color.WHITE);
        optionsButton.setBackground(Color.DARK_GRAY);
        optionsButton.setHorizontalAlignment(JButton.CENTER);
        optionsButton.setVerticalAlignment(JButton.CENTER);
        optionsButton.setFont(new Font("Comic Sans", Font.BOLD, 25));
    }

    /* -------- Methods for constructing the panels of the frame -------- */
    private void createTopPanel() {
        topPanel = new JPanel();
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.setSize(new Dimension());
        topPanel.setLayout(new GridLayout());
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setSize(new Dimension());
        mainPanel.setLayout(new GridLayout(0, 3)); // Columns are name, username, password
    }

    private void createBottomPanel() {
        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.DARK_GRAY);
//        bottomPanel.setBounds(0, 900, 750, 100);
        bottomPanel.setSize(new Dimension());
        bottomPanel.setLayout(new GridLayout());
        bottomPanel.add(optionsButton);
        bottomPanel.add(exitButton);

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createMainFrame(String username) {
        this.setLayout(new BorderLayout()); // Using a border layout to construct the frame
        this.setTitle("Locals - " + username); // Set title for the frame
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // If the main frame is closed, the whole program should close
//        this.setBackground(Theme.getMainFrameColor()); // Set the background color of the main frame based on the current theme
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT); // Set default size of the main frame
        this.setLocationRelativeTo(null); // Start the frame in the center of the display

        // Set icon for the frame
        ImageIcon localsIcon = new ImageIcon("LocalsIcon.png"); // TODO: get/make an icon
        this.setIconImage(localsIcon.getImage());

        // Add panels to the frame
        this.add(topPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        this.setVisible(true); // Make the frame visible
    }

    // Methods for loginButton pressed operations
    private void exitButtonPressed() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void optionsButtonPressed() {
        new OptionsFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exitButton) {
            exitButtonPressed();
        } else if (e.getSource() == optionsButton) {
            optionsButtonPressed();
        }
    }
}
