/*
 * NAME: Theme
 * AUTHOR: J. Pisani
 * DATE: 8/3/2024
 *
 * DESCRIPTION: Abstract class that will keep track of the theme of the program during runtime (light or dark). Methods
 * will return the correct color for the object (frame, loginButton, etc.) based on the theme that is set.
 */

package Frames;

import java.awt.*;

public abstract class Theme {
    // Constants
    private static final boolean LIGHT_MODE = true;
    private static final boolean DARK_MODE = false;

    private static boolean theme = LIGHT_MODE; // set the program in light mode normally

    // Public setters and getters for the current theme
    public static boolean getTheme() {
        return theme;
    }

    public static void setTheme(boolean newTheme) {
        // Make sure the theme is being switched to a valid theme
        if(newTheme != LIGHT_MODE && newTheme != DARK_MODE) {
            System.out.println("Invalid theme, keeping current theme");
        }
        else {
            // Only switch if new theme is different from the current theme
            if(theme != newTheme) {
                theme = newTheme;
            }
        }
    }

    // Public getters for colors for the objects based on theme
    public static Color getFrameColor() {
        if(theme == LIGHT_MODE) {
            return Color.WHITE;
//            return new Color(0xFFFFFF); // For a custom color using hex code or RGB (R,G,B)
        } else {
            return Color.DARK_GRAY;
        }
    }

    public static Color getButtonColor() {
        if(theme == LIGHT_MODE) {
            return Color.WHITE;
        } else {
            return Color.DARK_GRAY;
        }
    }

    public static Color getTextColor() {
        if(theme == LIGHT_MODE) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    // TODO: way to update the frame when the theme changes. Method that is triggered after theme is pressed?
}
