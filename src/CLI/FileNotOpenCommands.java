/*
 * NAME: FileNotOpenCommands
 * AUTHOR: J. Pisani
 * DATE: 10/3/24
 *
 * DESCRIPTION: Contains commands for the shell to run when a password file is not open
 */

package CLI;

abstract class FileNotOpenCommands {
    static void createNewPasswordFile() {
        if(!Shell.fileIsOpen) {

        } else {
            printFileOpenErrorMsg();
        }
    }

    static void deletePasswordFile() {
        if(!Shell.fileIsOpen) {

        } else {
            printFileOpenErrorMsg();
        }
    }

    static void openPasswordFile() {
        if(!Shell.fileIsOpen) {

        } else {
            printFileOpenErrorMsg();
        }
    }

    private static void printFileOpenErrorMsg() {
        System.out.println("ERROR: a password file is currently open. Please close it before using the \"" + Shell.input + "\" command.");
    }
}
