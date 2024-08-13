/*
 * NAME: LaunchFrame
 * AUTHOR: J. Pisani
 * DATE: 8/8/2024
 *
 * DESCRIPTION: The first frame that appears on launch of the program. Allows the user to either login or create a new
 * user. After doing either, the main frame will launch with the user's passwords and information.
 */

package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class LaunchFrame extends JFrame implements ActionListener {
    // Panels
    private JPanel mainPanel;

    // Buttons
    private JButton loginButton;
    private JButton newUserButton;


    // Construct the Launch Frame
    public LaunchFrame() {
        createLogInButton();
        createMainPanel();

        this.setTitle("Locals"); // Set title of launch frame
        this.setSize(750, 750); // Set size of frame
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // If the user closes this frame, end the program
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null); // Set launch frame to appear in center of screen

        // Set icon for the frame
        ImageIcon localsIcon = new ImageIcon("LocalsIcon.png"); // TODO: create and add icon to project
        this.setIconImage(localsIcon.getImage());

        this.add(mainPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    /* -------- Constructing methods for the panels of the frame -------- */
    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setSize(new Dimension());
        mainPanel.setLayout(new GridLayout());
        mainPanel.add(loginButton);
    }

    /* -------- Constructing methods for the buttons of the frame -------- */
    private void createLogInButton() {
        loginButton = new JButton("LOGIN");
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setFocusable(false);
        loginButton.setHorizontalAlignment(JButton.CENTER);
        loginButton.setVerticalAlignment(JButton.CENTER);
        loginButton.setFont(new Font("Comic Sans", Font.BOLD, 25));
        loginButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
        loginButton.addActionListener(this);
    }

    private boolean loginButtonPressed() {
        // return User.login(username, password); // Prototype for method in User to login
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loginButton) {
            // If login was successful
            if(loginButtonPressed()) {
                // REVIEW: should we open a new window after login or change and keep the same frame? leaning towards keeping it in the same frame
                this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Before we close this frame, we must change the close operation to avoid ending the program
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)); // Close this window
                new MainFrame("username"); // Open the main from with the user's information
            } else {
                // Login unsuccessful
                JLabel failedLogInLabel = new JLabel("Log in failed: Incorrect username or password");
                failedLogInLabel.setForeground(Color.RED);
                failedLogInLabel.setBackground(Color.DARK_GRAY);
            }
        }
    }
}
