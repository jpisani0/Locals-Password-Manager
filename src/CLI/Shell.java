/*
 * NAME: Shell
 * AUTHOR: J. Pisani
 * DATE: 10/3/24
 *
 * DESCRIPTION: Shell that is run when Locals is called to run without GUI
 */

package CLI;

import java.util.Objects;
import java.util.Scanner;

public class Shell {
    private boolean exit = false; // Flag for when the user wants to exit the program
    static String fileName = ""; // Name of file that is currently open
    private boolean fileIsOpen = false; // Flag to check if a password file is open
    private static String input = "";

    Shell() {
        // TODO: add some other information to print when starting such as the version number of the program

        System.out.println("WARNING: viewing decrypted passwords in CLI will cause them to be logged in plain text on" +
                " your machine. Consider deleting them from your logs for maximum security.");

        Scanner scanner = new Scanner(System.in);

        while(!exit) {
            if(fileIsOpen) {
                System.out.print("\n>> (" + fileName + ") ");
            } else {
                System.out.print("\n>> ");
            }

            input = scanner.nextLine().toLowerCase(); // Wait for the user's input

            // Determine the command the user entered for what to do next
            switch(input) {
                case "exit":
                    exit = true;
                    break;

                case "":
                    // Do nothing if the user entered nothing
                    break;

                case "n":
                case "new":
                    if(!fileIsOpen) {
                        FileNotOpenCommands.createNewPasswordFile();
                    } else {
                        printFileOpenErrorMsg();
                    }
                    break;

                case "d":
                case "delete":
                    if(!fileIsOpen) {
                        FileNotOpenCommands.deletePasswordFile();
                    } else {
                        printFileOpenErrorMsg();
                    }
                    break;

                case "o":
                case "open":
                    fileName = FileNotOpenCommands.openPasswordFile();

                    // If the file name was not updated, then the file was not opened successfully
                    if(!Objects.equals(fileName, "")) {
                        fileIsOpen = true;
                    }
                    break;

                case "c":
                case "close":
                    if(fileIsOpen) {
                        fileName = FileOpenCommands.closePasswordFile();

                        // If the file name was not cleared
                        if(Objects.equals(fileName, "")) {
                            fileIsOpen = false;
                        }
                    } else {
                        printFileNotOpenErrorMsg();
                    }
                    break;

                case "l":
                case "list":
                    if(fileIsOpen) {
                        FileOpenCommands.listEntries();
                    } else {
                        printFileNotOpenErrorMsg();
                    }
                    break;

                case "a":
                case "add":
                    if(fileIsOpen) {
                        FileOpenCommands.addNewEntry();
                    } else {
                        printFileNotOpenErrorMsg();
                    }
                    break;

                case "r":
                case "remove":
                    if(fileIsOpen) {
                        FileOpenCommands.removeEntry();
                    } else {
                        printFileNotOpenErrorMsg();
                    }
                    break;

                default:
                    // Unknown command
                    System.out.println("ERROR: Unknown command: " + input + ".");
                    printHelp();
                    break;
            }
        }

        System.out.println("Exiting Locals...");
    }


    // Print out methods for repeating print statements
    private void printHelp() {
        // TODO: manual type print out for all commands depending on whether the file is open or not
    }

    private void printFileNotOpenErrorMsg() {
        System.out.println("ERROR: no password file is open. Please open one before using the \"" + input + "\" command.");
    }

    private void printFileOpenErrorMsg() {
        System.out.println("ERROR: a password file is currently open. Please close it before using the \"" + input + "\" command.");
    }
}
