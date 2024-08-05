/*
 * NAME: OptionsFrame
 * AUTHOR: J. Pisani
 * DATE: 8/5/2024
 *
 * DESCRIPTION: Creates the options frame for the program
 */

package Frames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsFrame extends JFrame implements ActionListener {
    private static final int FRAME_WIDTH = 750;
    private static final int FRAME_HEIGHT = 750;

    public OptionsFrame() {
        this.setVisible(true); // Set the frame to be visible
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle("Options");
        this.setBackground(Theme.getMainFrameColor());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
