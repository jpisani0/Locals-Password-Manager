/*
 * NAME: Shell
 * AUTHOR: J. Pisani
 * DATE: 10/3/24
 *
 * DESCRIPTION: Shell that is run when Locals is called to run without GUI
 */

package CLI;

import java.util.Scanner;

abstract public class Shell {
    private static boolean exit = false; // Flag for when the user wants to exit the program
    private static String fileName = ""; // Name of file that is currently open
    private static boolean fileIsOpen = false; // Flag to check if a password file is open
    private static String input = ""; // User input on command line
    private static final Scanner scanner = new Scanner(System.in); // Scanner for user input on command line

    public static void start() {
        // TODO: add some other information to print when starting such as the version number of the program

        System.out.println("WARNING: viewing decrypted passwords in CLI will most likely be logged in plain text on" +
                " your machine. Consider deleting them from your logs for maximum security.");

        runShell(); // Runs until the user requests to exit

        System.out.println("Exiting Locals...");
    }

    // Runs the shell
    private static void runShell() {
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
    private static void runCommand() {
        switch(input) {
            case "exit":
                // Close the open password file if one is open before exiting
                if(fileIsOpen) {
                    closePasswordFile();
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
                createNewPasswordFile();
                break;

            case "d":
            case "delete":
                deletePasswordFile();
                break;

            case "o":
            case "open":
                openPasswordFile();
                break;

            case "c":
            case "close":
                closePasswordFile();
                break;

            case "l":
            case "list":
                listEntries();
                break;

            case "s":
            case "show":
                showEntry();
                break;

            case "a":
            case "add":
                addNewEntry();
                break;

            case "r":
            case "remove":
                removeEntry();
                break;

            default:
                // Unknown command
                System.out.println("ERROR: Unknown command: " + input + ".\n");
                printHelp();
                break;
        }
    }

    // Manual type print out when requested or an invalid command is entered
    private static  void printHelp() {
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


    /* -------------------------------------------------------------------------------- */
    /* ------------------------------ FILE OPEN COMMANDS ------------------------------ */
    /* -------------------------------------------------------------------------------- */
    private  static void closePasswordFile() {
        if(Shell.fileIsOpen) {

        } else {
            printFileNotOpenErrorMsg();
        }
    }

    private static void listEntries() {
        if(Shell.fileIsOpen) {

        } else {
            printFileNotOpenErrorMsg();
        }
    }

    private static void showEntry() {
        if(Shell.fileIsOpen) {

        } else {
            printFileNotOpenErrorMsg();
        }
    }

    private static void addNewEntry() {
        if(Shell.fileIsOpen) {

        } else {
            printFileNotOpenErrorMsg();
        }
    }

    private static void removeEntry() {
        if(Shell.fileIsOpen) {

        } else {
            printFileNotOpenErrorMsg();
        }
    }

    private static void printFileNotOpenErrorMsg() {
        System.out.println("ERROR: no password file is open. Please open one before using the \"" + Shell.input + "\" command.");
    }


    /* -------------------------------------------------------------------------------- */
    /* ---------------------------- FILE NOT OPEN COMMANDS ---------------------------- */
    /* -------------------------------------------------------------------------------- */
    private static void createNewPasswordFile() {
        if(!Shell.fileIsOpen) {

        } else {
            printFileOpenErrorMsg();
        }
    }

    private static void deletePasswordFile() {
        if(!Shell.fileIsOpen) {

        } else {
            printFileOpenErrorMsg();
        }
    }

    private static void openPasswordFile() {
        if(!Shell.fileIsOpen) {

        } else {
            printFileOpenErrorMsg();
        }
    }

    private static void printFileOpenErrorMsg() {
        System.out.println("ERROR: a password file is currently open. Please close it before using the \"" + Shell.input + "\" command.");
    }
}
