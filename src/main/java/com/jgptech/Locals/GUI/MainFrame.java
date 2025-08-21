/*
 * NAME: MainFrame
 * DATE: 6/26/25
 * AUTHOR: J. Pisani
 *
 * DESCRIPTION: The main frame for the program. Stays up for life of process and all other frames start from here.
 */

package com.jgptech.Locals.GUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Locals");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // REVIEW: change this in the future to instead run some logic before closing so that we can ensure everything saves properly before exiting
        setResizable(false); // REVIEW: let them resize?
        setLocationRelativeTo(null);

        // TODO: make icon for program
        ImageIcon image = new ImageIcon(MainFrame.class.getResource("/icons/logo.png"));
        setIconImage(image.getImage());

        getContentPane().setBackground(Color.darkGray);


        setVisible(true);
    }
}
