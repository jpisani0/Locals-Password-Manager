/*
 * NAME: Shell
 * AUTHOR: J. Pisani
 * DATE: 10/3/24
 *
 * DESCRIPTION: Shell for viewing and modifying a vault file
 */

package com.jgptech.Locals.CLI;

import com.jgptech.Locals.Vault.Vault;

import javax.crypto.SecretKey;
import java.awt.*;
import java.util.Scanner;

public class Shell {
    // Flag for when the user wants to exit the program
    private boolean exit = false;

    // User input on command line
    private String input = "";

    // Scanner for user input on command line
    private final Scanner scanner = new Scanner(System.in);

    // The vault that is open
    private final Vault vault;

    // The group that is currently selected
    private int groupIndex;

    // Key for decryption of vault data
    private final SecretKey key;


    // Constructor for the shell class
    public Shell(Vault vault, SecretKey key) {
        this.vault = vault;
        this.key = key;
        this.groupIndex = 0;
    }

    // Start the shell
    public void start() {
        runShell(); // Runs until the user requests to exit
    }

    // Runs the shell
    private void runShell() {
        while(!exit) {
            System.out.print("(" + vault.getName() + " | " + vault.getGroupName(groupIndex, key) + ") >> ");
            input = scanner.nextLine().toLowerCase(); // Wait for the user's input
            String[] words = input.trim().split("\\s+"); // Split the input per word

            try {
                runCommand(words);
            } catch(IndexOutOfBoundsException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    // Determine the command the user entered for what to do next
    private void runCommand(String[] words) throws IndexOutOfBoundsException {
        switch(words[0]) {
            case "exit":
            case "quit":
            case "q":
                exit = true;
                break;

            case "":
                // Do nothing if the user entered nothing
                break;

            case "h":
            case "help":
                printHelp();
                break;

            case "l":
            case "list":
                list(words);
                break;

            case "o":
            case "open":
                open(words);
                break;

            case "s":
            case "show":
                show(words);
                break;

            case "a":
            case "add":
                add(words);
                break;

            case "r":
            case "remove":
                remove(words);
                break;

            case "m":
            case "move":
                move(words);
                break;

            default:
                // Unknown command
                System.out.println("ERROR: Unknown command: " + input + ".\n");
                printHelp();
                break;
        }
    }

    // Manual type print out when requested or an invalid command is entered
    private  void printHelp() {
        System.out.println(
                "exit\n" +
                        "\tclose and exit the vault.\n" +
                "h, help\n" +
                        "\tprint these commands and their function.\n" +
                "l, list\n" +
                        "\tlist all groups in the vault or entries in the group\n" +
                "o, open\n" +
                        "\topen a group in the vault" +
                "s, show\n" +
                        "\tshow the saved data in an entry\n" +
                "a, add\n" +
                        "\tadd a new entry or group to the vault\n" +
                "r, remove\n" +
                        "\tremove an entry or group from the vault\n" +
                "m, move\n" +
                        "\tmove an entry to another group\n"
        );
    }

    // List the groups in the vault or entries in the vault
    private void list(String[] words) {
        // Check if the user wants to list groups or entries
        if(words[1].equals("groups") || words[1].equals("group")) {
            vault.listGroups(key);
        } else if(words[1].equals("entries") || words[1].equals("entry")) {
            vault.listEntries(groupIndex, key);
        } else {
            System.out.println("ERROR: use 'list groups' or 'list entries'\n");
            printHelp();
        }
    }

    // Open a group in the vault
    private void open(String[] words) {
        // TODO: also allow the user to pass the name of the group to select it
        int newGroupIndex = Integer.parseInt(words[1]);

        if(newGroupIndex < 0 || newGroupIndex > vault.size()) {
            System.out.println("ERROR: invalid group index: " + words[1] + ". There are " + vault.size() + " groups in this vault.");
        } else {
            groupIndex = Integer.parseInt(words[1]);
        }
    }

    // Show an entry's data
    private void show(String[] words) {
        int entryIndex = Integer.parseInt(words[1]);

        System.out.println();
        System.out.println("Name: " + vault.getEntryName(groupIndex, entryIndex, key));
        System.out.println("Username: " + vault.getEntryUsername(groupIndex, entryIndex, key));
        System.out.println("Password: " + vault.getEntryPassword(groupIndex,entryIndex, key));
        System.out.println("URL: " + vault.getEntryUrl(groupIndex, entryIndex, key));
        System.out.println("Notes: " + vault.getEntryNotes(groupIndex, entryIndex, key));
        System.out.println();
    }

    // Add a group or entry to the vault
    private void add(String[] words) {
        // Check if the user wants to add a group or entry
        if(words[1].equals("group") || words[1].equals("groups")) {
            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Color: ");
            Color color = Color.RED; // TODO: put switch statement to get color from user, just using red for all for now

            vault.addGroup(name, color, key);
        } else if(words[1].equals("entry") || words[1].equals("entries")) {
            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.print("URL: ");
            String url = scanner.nextLine();

            System.out.print("Notes: ");
            String notes = scanner.nextLine();

            vault.addEntry(groupIndex, key, name, username, password, url, notes);
        } else {
            System.out.println("ERROR: use 'add group' or 'add entry'");
        }
    }

    // Remove a group or entry from the vault
    private void remove(String[] words) {
        // Check if a user wants to remove a group or entry
        if(words[1].equals("group") || words[1].equals("groups")) {
            vault.removeGroup(Integer.parseInt(words[2]));
        } else if(words[1].equals("entry") || words[1].equals("entries")) {
            vault.removeEntry(groupIndex, Integer.parseInt(words[2]));
        } else {
            System.out.println("ERROR: use 'remove group <groupIndex>' or 'remove entry <entryIndex>'");
        }
    }

    // Move an entry from one group to another
    private void move(String[] words) {
        // TODO: add method to move groups in vault
        int entryIndex = Integer.parseInt(words[1]);
        int fromGroupIndex = Integer.parseInt(words[2]);
        int toGroupIndex = Integer.parseInt(words[3]);


        vault.moveEntry(fromGroupIndex, toGroupIndex, entryIndex);
    }
}
