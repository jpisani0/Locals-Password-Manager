/*
 * NAME: FileOpenCommands
 * AUTHOR: J. Pisani
 * DATE: 10/2/24
 *
 * DESCRIPTION: Contains commands to run in the shell when a password file is open
 */

package CLI;

abstract class FileOpenCommands {
    // TODO: create functionality for these commands once the file manipulation and encryption/decryption classes are done
    static void closePasswordFile() {
        if(Shell.fileIsOpen) {

        } else {
            printFileNotOpenErrorMsg();
        }
    }

    static void listEntries() {
        if(Shell.fileIsOpen) {

        } else {
            printFileNotOpenErrorMsg();
        }
    }

    static void addNewEntry() {
        if(Shell.fileIsOpen) {

        } else {
            printFileNotOpenErrorMsg();
        }
    }

    static void removeEntry() {
        if(Shell.fileIsOpen) {

        } else {
            printFileNotOpenErrorMsg();
        }
    }

    private static void printFileNotOpenErrorMsg() {
        System.out.println("ERROR: no password file is open. Please open one before using the \"" + Shell.input + "\" command.");
    }
}
