/*
 * NAME: Shell
 * AUTHOR: J. Pisani
 * DATE: 10/3/24
 *
 * DESCRIPTION: Shell that is run when Locals is called to run without GUI
 */

package CLI;

import java.util.Scanner;

public class Shell {
    private boolean exit = false; // Flag for when the user wants to exit the program
    static String fileName = ""; // Name of file that is currently open
    static boolean fileIsOpen = false; // Flag to check if a password file is open
    static String input = ""; // User input on command line
    private final Scanner scanner = new Scanner(System.in); // Scanner for user input on command line

    public Shell() {
        // TODO: add some other information to print when starting such as the version number of the program

        System.out.println("WARNING: viewing decrypted passwords in CLI will most likely be logged in plain text on" +
                " your machine. Consider deleting them from your logs for maximum security.");

        runShell(); // Runs until the user requests to exit

        System.out.println("Exiting Locals...");
    }

    // Runs the shell
    private void runShell() {
        while(!exit) {
            if(fileIsOpen) {
                System.out.print(">> (" + fileName + ") ");
            } else {
                System.out.print(">> ");
            }

            input = scanner.nextLine().toLowerCase(); // Wait for the user's input

            runCommand();
        }
    }

    // Determine the command the user entered for what to do next
    private void runCommand() {
        switch(input) {
            case "exit":
                // Close the open password file if one is open before exiting
                if(fileIsOpen) {
                    FileOpenCommands.closePasswordFile();
                }

                exit = true;
                break;

            case "":
                // Do nothing if the user entered nothing
                break;

            case "h":
            case "help":
                printHelp();
                break;

            case "n":
            case "new":
                FileNotOpenCommands.createNewPasswordFile();
                break;

            case "d":
            case "delete":
                FileNotOpenCommands.deletePasswordFile();
                break;

            case "o":
            case "open":
                FileNotOpenCommands.openPasswordFile();
                break;

            case "c":
            case "close":
                FileOpenCommands.closePasswordFile();
                break;

            case "l":
            case "list":
                FileOpenCommands.listEntries();
                break;

            case "s":
            case "show":
                FileOpenCommands.showEntry();
                break;

            case "a":
            case "add":
                FileOpenCommands.addNewEntry();
                break;

            case "r":
            case "remove":
                FileOpenCommands.removeEntry();
                break;

            default:
                // Unknown command
                System.out.println("ERROR: Unknown command: " + input + ".");
                printHelp();
                break;
        }
    }

    // Manual type print out when requested or an invalid command is entered
    private void printHelp() {
        System.out.println(
                "exit\n" +
                        "   close and exit Locals. If a password file is open when exit is called, it will be closed automatically.\n" +
                "h, help\n" +
                        "   print these commands and their function to CLI.\n" +
                "n, new\n" +
                        "   create a new encrypted password file.\n" +
                "d, delete\n" +
                        "   delete an encrypted password file\n" +
                "o, open\n" +
                        "   open an existing encrypted password file\n" +
                "c, close\n" +
                        "   close the password file that is currently open\n" +
                "l, list\n" +
                        "   list all entries of the open password file\n" +
                "s, show\n" +
                        "   show the username and password saved in a certain entry\n" +
                "a, add\n" +
                        "   add a new entry to an open password file\n" +
                "r, remove\n" +
                        "   remove an entry from an open password file\n"
        );
    }
}