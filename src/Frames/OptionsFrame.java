/*
 * NAME: OptionsFrame
 * AUTHOR: J. Pisani
 * DATE: 8/5/2024
 *
 * DESCRIPTION: Creates the options frame for the program
 */

package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsFrame extends JFrame implements ActionListener {
    private static final int FRAME_WIDTH = 750;
    private static final int FRAME_HEIGHT = 750;

    public OptionsFrame() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // When this window is closed kill it but do not end the program
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Options");
        this.setLocationRelativeTo(null);
//        this.setBackground(Theme.getMainFrameColor());
        this.setBackground(new Color(0, 0, 0));
        this.setVisible(true); // Set the frame to be visible
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
