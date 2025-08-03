/*
 * NAME: Shell
 * AUTHOR: J. Pisani
 * DATE: 10/3/24
 *
 * DESCRIPTION: Shell for viewing and modifying a vault file
 */

package com.jgptech.Locals.CLI;

import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.PasswordGenerator;
import com.jgptech.Locals.Vault.*;

import javax.crypto.SecretKey;
import javax.print.attribute.standard.PrinterMessageFromOperator;
import java.awt.*;
import java.io.Console;
import java.util.Scanner;

public class Shell {
    // To show that a given index is invalid
    private final int INVALID_INDEX = -1;

    // Flag for when the user wants to exit the program
    private boolean exit = false;

    // User input on command line
    private String input = "";

    // Scanner for user input on command line
    private final Scanner scanner = new Scanner(System.in);

    // Handle for the console
    private final Console console = System.console();

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
            System.out.print("(" + vault.getName() + " | " + vault.getGroup(groupIndex).getName(key, vault.getEncryptionAlgorithm()) + ") >> ");
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

            case "d":
            case "delete":
                delete(words);
                break;

            case "e":
            case "edit":
                edit(words);
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
    private void printHelp() {
        System.out.println(
                """
                
                exit
                    close and exit the vault.
                        [exit|quit]
                h, help
                    print these commands and their function.
                        help
                l, list
                    list all groups in the vault or entries in the group
                        list [groups|entries]
                o, open
                    open a group in the vault
                        open [group]
                s, show
                    show the saved data in an entry
                        show [entry]
                a, add
                    add a new entry or group to the vault
                        add [entry|group]
                d, delete
                    delete an entry or group from the vault
                        delete [entry|group]
                e, edit
                    edit the data in an entry or group
                        edit entry [entry] [name|username|password|URL|notes]
                        edit group [group] [name|color]
                m, move
                    move an entry to another group
                        move [entry] [new-group]
                """
        );
    }

    // Prints out an error message along with the help list
    private void printErrorMsg(String errorMsg) {
        System.out.println(errorMsg);
        printHelp();
    }

    // List the groups in the vault or entries in the vault
    private void list(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 1) {
            // Check if the user wants to list groups or entries
            if(isGroupSelected(words[1])) {
                vault.listGroups(key);
            } else if(isEntrySelected(words[1])) {
                vault.listEntries(groupIndex, key);
            } else {
                printErrorMsg("ERROR: use 'list groups' or 'list entries'");
            }
        } else {
            printErrorMsg("ERROR: use 'list groups' or 'list entries'");
        }
    }

    // Open a group in the vault
    private void open(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 1) {
            openGroup(words[1]);
        } else {
            printErrorMsg("ERROR: add the group name or number that you wish to open: 'open <group>'");
        }
    }

    // Show an entry's data
    private void show(String[] words) {
        // Check that the user supplied the second argument
        if(words.length > 1) {
            showEntry(words[1]);
        } else {
            printErrorMsg("ERROR: add the entry name or number you wish to show: 'show <entry>'");
        }
    }

    // Add a group or entry to the vault
    private void add(String[] words) {
        // Check tha the user supplied the second argument
        if(words.length > 1) {
            // Check if the user wants to add a group or entry
            if(isGroupSelected(words[1])) {
                addGroup();
            } else if(isEntrySelected(words[1])) {
                addEntry();
            } else {
                printErrorMsg("ERROR: use 'add group' or 'add entry'");
            }

            // Write this to the vault to avoid data loss in the event the shell does not close properly
            vault.write();
        } else {
            printErrorMsg("ERROR: use 'add group' or 'add entry'");
        }
    }

    // Remove a group or entry from the vault
    private void delete(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 2) {
            // Check if a user wants to remove a group or entry
            if(isGroupSelected(words[1])) {
                deleteGroup(words[2]);
            } else if(isEntrySelected(words[1])) {
                deleteEntry(words[2]);
            } else {
                printErrorMsg("ERROR: use 'delete group <group-number>' or 'delete entry <entry-number>'");
            }

            // Write this to the vault to avoid data loss in the event the shell does not close properly
            vault.write();
        } else {
            printErrorMsg("ERROR:  use 'delete group <group-number>' or 'delete entry <entry-number>'");
        }
    }

    // Edit data in an entry
    private void edit(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 3) {
            if(isGroupSelected(words[1])) {
                editGroup(words[2], words[3]);
            } else if(isEntrySelected(words[1])) {
                editEntry(words[2], words[3]);
            } else {
                printErrorMsg("ERROR: Use 'edit group <group> <field>' or 'edit entry <entry> <field>'.");
            }
        } else {
            printErrorMsg("ERROR: Use 'edit group <group> <field>' or 'edit entry <entry> <field>'.");
        }
    }

    // Move an entry from one group to another
    private void move(String[] words) {
        // Check that the user supplied all needed arguments
        if(words.length > 3) {
            if(isGroupSelected(words[1])) {
                // TODO look into the vault.moveGroups() method, has weird behavior and still needs to be figured out
                moveGroup(words[2], words[3]);
            } else if(isEntrySelected(words[1])) {
                moveEntry(words[2], words[3]);
            } else {
                printErrorMsg("ERROR: use 'move group <group> <new-index>' or 'move entry <entry> <new-group>");
            }
        } else {
            printErrorMsg("ERROR: use 'move group <group> <new-index>' or 'move entry <entry> <new-index>' or 'move entry <entry> <new-group>");
        }
    }

    // Open a group in the vault
    private void openGroup(String word) {
        int newGroupIndex = INVALID_INDEX;

        // Check if the user entered the group number
        try {
            newGroupIndex = Integer.parseInt(word) - 1;

            // Check that this is a valid group index
            if(newGroupIndex < 0 || newGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + word + " is not a valid group number. Use 'list groups' to see all group names and numbers.");
                newGroupIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the name of a group
            for(int index = 0; index < vault.size(); index++) {
                if(word.equals(vault.getGroup(groupIndex).getName(key, vault.getEncryptionAlgorithm()).toLowerCase())) {
                    newGroupIndex = index;
                    break;
                }
            }

            // Check if the name was found in this vault
            if(newGroupIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + word + " is not the name of a group in this vault. Use 'list groups' to see all group names and numbers.");
            }
        }

        // Only update the group index if the entered one was valid
        if(newGroupIndex != INVALID_INDEX) {
            groupIndex = newGroupIndex;
        }
    }

    // Display information from an entry
    private void showEntry(String word) {
        int entryIndex = INVALID_INDEX;

        // Check if the user entered an entry number
        try {
            entryIndex = Integer.parseInt(word) - 1;

            if(entryIndex < 0 || entryIndex > vault.getGroup(groupIndex).size() - 1) {
                printErrorMsg("ERROR: " + word + " is not a valid entry number. Use 'list entries' to see all entry names and numbers.");
                entryIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the name of an entry in this group
            for(int index = 0; index < vault.getGroup(groupIndex).size(); index++) {
                if(word.equals(vault.getGroup(groupIndex).getEntry(index).getName(key, vault.getEncryptionAlgorithm()).toLowerCase())) {
                    entryIndex = index;
                    break;
                }
            }

            // Check if the name was found in this group
            if(entryIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + word + " is not the name of an entry in this group. Use 'list entries' to see all entry names and numbers.");
            }
        }

        // Check that a valid entry index was given
        if(entryIndex != INVALID_INDEX) {
            vault.getGroup(groupIndex).getEntry(entryIndex).print();
        }
    }

    // Add a new group to the vault
    private void addGroup() {
        System.out.print("Name: ");
        String name = scanner.nextLine();

//                System.out.print("Color: ");
        Color color = Color.RED; // TODO: put switch statement to get color from user, just using red for all for now

        vault.addGroup(new Group(name, color, key, vault.getEncryptionAlgorithm()));
    }

    // Add an entry to the group
    private void addEntry() {
        final int LOGIN = 1;
        final int PAYMENT_CARD = 2;
        final int SSH_KEY = 3;
        final int SECURE_NOTE = 4;

        int entryType = 0; // TODO: add entry type enum (?)

        while(entryType == 0) {
            System.out.println("1. Login");
            System.out.println("2. Payment Card");
            System.out.println("3. SSH Key");
            System.out.println("4. Secure Note");
            System.out.println();
            System.out.print("Choose entry type: ");

            String response = scanner.nextLine();

            try {
                entryType = Integer.parseInt(response);

                if(entryType < LOGIN || entryType > SECURE_NOTE) {
                    throw new NumberFormatException("Invalid entry type");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: " + response + " is not a valid entry type.");
                System.out.println();
            }
        }

        switch(entryType) {
            case LOGIN:
                addLogin();
                break;

            case PAYMENT_CARD:
                addPaymentCard();
                break;

            case SSH_KEY:
                addSSHKey();
                break;

            case SECURE_NOTE:
                addSecureNote();
                break;

            default:
                // We should never reach this case, here for debugging purposes
                System.out.println("Error: " + entryType + " is not a valid entry type");
                break;
        }
    }

    // Add a Login to the group
    private void addLogin() {
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        // See if the user wants to enter their own password or generate a random one
        System.out.print("Do you want to generate a random password? [y/N]: ");
        String input = scanner.nextLine();
        String password = "";

        if(input.equals("n") || input.equals("N") || input.isEmpty()) {
            while(password.isEmpty()) {
                password = new String(console.readPassword("Password: "));

                String verifiedPassword = new String(console.readPassword("Verify password: "));

                if(!password.equals(verifiedPassword)) {
                    System.out.println("Passwords did not match, please try again");
                    password = "";
                }
            }
        } else {
            int length = 0;

            while(length < 1) {
                System.out.print("Length of randomly generated password: ");

                try {
                    length = Integer.parseInt(scanner.nextLine());

                    // REVIEW: 512 good max for password length?
                    // Check t        vault.addEntry(groupIndex, key, name, username, password, url, notes);hat this is a valid length
                    if(length < 1 || length > 512) {
                        throw new NumberFormatException("Out of range");
                    }
                } catch(NumberFormatException e) {
                    printErrorMsg("Please enter a valid length for the password");
                }
            }

            while(password.isEmpty()) {
                password = PasswordGenerator.generatePassword(length);
                System.out.println("Your randomly generated password is: " + password);
                System.out.print("Do you want to regenerate this password? [Y/n]: ");

                input = scanner.nextLine();

                if(input.equals("Y") || input.equals("y") || input.isEmpty()) {
                    password = "";
                }
            }
        }

        System.out.print("URL: ");
        String url = scanner.nextLine();

        System.out.print("Notes: ");
        String notes = scanner.nextLine();

        vault.getGroup(groupIndex).getEntries().add(new Login(name, username, password, url, notes, key, vault.getEncryptionAlgorithm()));
    }

    // Add a payment card to the group
    private void addPaymentCard() {
        String name = "";
        String brand = "";
        String cardholderName = "";
        String cardNumber = "";
        String expireDate = "";
        String securityCode = "";
        String notes = "";

        while(name.isEmpty()) {
            System.out.print("Name: ");
            name = scanner.nextLine();
        }

        while(brand.isEmpty()) {
            System.out.print("Brand: ");
            brand = scanner.nextLine();
        }

        while(cardholderName.isEmpty()) {
            System.out.print("Cardholder Name: ");
            cardholderName = scanner.nextLine();
        }

        while(cardNumber.isEmpty()) {
            System.out.print("Card number: ");
            cardNumber = scanner.nextLine();
        }

        while(expireDate.isEmpty()) {
            System.out.print("Expiration Date (DD/MM): ");
            expireDate = scanner.nextLine();
        }

        while(securityCode.isEmpty()) {
            System.out.print("Security Code: ");
            securityCode = scanner.nextLine();
        }

        while(notes.isEmpty()) {
            System.out.print("Notes: ");
            notes = scanner.nextLine();
        }

        vault.getGroup(groupIndex).getEntries().add(new PaymentCard(name, brand, cardholderName, cardNumber, expireDate, securityCode, notes, key, vault.getEncryptionAlgorithm()));
    }

    // Add an SSH Key to the group
    private void addSSHKey() {
        String name = "";
        String privateKey = "";
        String publicKey = "";
        String fingerprint = "";
        String notes = "";

        while(name.isEmpty()) {
            System.out.print("Name: ");
            name = scanner.nextLine();
        }

        while(privateKey.isEmpty()) {
            System.out.print("Private key: ");
            privateKey = scanner.nextLine();
        }

        while(publicKey.isEmpty()) {
            System.out.print("Public key: ");
            publicKey = scanner.nextLine();
        }

        while(fingerprint.isEmpty()) {
            System.out.print("Fingerprint: ");
            fingerprint = scanner.nextLine();
        }

        while(notes.isEmpty()) {
            System.out.print("Notes: ");
            notes = scanner.nextLine();
        }

        vault.getGroup(groupIndex).getEntries().add(new SSHKey(name, privateKey, publicKey, fingerprint, key, vault.getEncryptionAlgorithm()));
    }

    // Add a secure note to the group
    private void addSecureNote() {
        String name = "";
        String notes = "";

        while(name.isEmpty()) {
            System.out.print("Name: ");
            name = scanner.nextLine();
        }

        while(notes.isEmpty()) {
            System.out.print("Note: ");
            notes = scanner.nextLine();
        }

        vault.getGroup(groupIndex).getEntries().add(new SecureNote(name, notes, key, vault.getEncryptionAlgorithm()));
    }

    // Delete a group from the vault
    private void deleteGroup(String word) {
        int removeGroupIndex = INVALID_INDEX;

        // Check if the user entered the group number
        try {
            removeGroupIndex = Integer.parseInt(word) - 1;

            // Check that it is a valid group index
            if(removeGroupIndex < 0 || removeGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + word + " is not a valid group number. Use 'list groups' to show all group names and numbers.");
                removeGroupIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the group name
            for(int index = 0; index < vault.size(); index++) {
                if(word.equals(vault.getGroup(index).getName(key, vault.getEncryptionAlgorithm()).toLowerCase())) {
                    removeGroupIndex = index;
                    break;
                }
            }

            // Check that the name was found in this vault
            if(removeGroupIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + word + " is not a valid group name. Use 'list groups' to show all group names and numbers.");
            }
        }

        // See if we found a valid group index
        if(removeGroupIndex != INVALID_INDEX) {
            vault.removeGroup(removeGroupIndex);
        }
    }

    // Delete an entry from the group
    private void deleteEntry(String word) {
        int removeEntryIndex = INVALID_INDEX;

        // Check if the user entered the entry number
        try {
            removeEntryIndex = Integer.parseInt(word) - 1;

            if(removeEntryIndex < 0 || removeEntryIndex > vault.getGroup(groupIndex).size() - 1) {
                printErrorMsg("ERROR: " + word + " is not a valid entry number. Use 'list entries' to show all entry names and numbers.");
                removeEntryIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered an entry name
            for(int index = 0; index < vault.getGroup(groupIndex).size(); index++) {
                if(word.equals(vault.getGroup(groupIndex).getEntry(index).getName(key, vault.getEncryptionAlgorithm()).toLowerCase())) {
                    removeEntryIndex = index;
                    break;
                }
            }

            // Check if the entry name was found
            if(removeEntryIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + word + " is not a valid entry name. Use 'list entries' to show all entry names and numbers.");
            }
        }

        // Check if a valid entry index was found
        if(removeEntryIndex != INVALID_INDEX) {
            vault.getGroup(groupIndex).getEntries().remove(removeEntryIndex);
        }
    }

    // Edit a group in the vault
    private void editGroup(String groupWord, String fieldWord) {
        int editGroupIndex = INVALID_INDEX;
        String field = "";
        int fieldNum = INVALID_INDEX;

        // Check if the user entered the group number
        try {
            editGroupIndex = Integer.parseInt(groupWord) - 1;

            // Check that this is a valid group index
            if(editGroupIndex < 0 || editGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group index. Use 'list groups' to show all group names and numbers.");
                editGroupIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the group number
            for(int index = 0; index < vault.size(); index++) {
                if(groupWord.equals(vault.getGroup(index).getName(key, vault.getEncryptionAlgorithm()).toLowerCase())) {
                    editGroupIndex = index;
                    break;
                }
            }

            // Check if the group index was found
            if(editGroupIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group name. Use 'list groups' to see all group names and numbers.");
            }
        }

        // Only continue if the group index is valid
        if(editGroupIndex != INVALID_INDEX) {
            switch(fieldWord) {
                case "name":
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    vault.getGroup(editGroupIndex).setName(name, key, vault.getEncryptionAlgorithm());
                    break;

                case "color":
                    System.out.println("Color not supported yet!");
                    break;

                default:
                    printErrorMsg("ERROR: " + fieldWord + " is not a valid field.");
                    break;
            }

            vault.write();
        }
    }

    // Edit an entry in the vault
    private void editEntry(String entryWord, String fieldWord) {
        int entryIndex = INVALID_INDEX;

        // REVIEW: maybe this can be made as a method for the Group class?
        // Check if the user entered the entry number
        try {
            entryIndex = Integer.parseInt(entryWord) - 1;

            // Check that this is a valid entry index
            if(entryIndex < 0 || entryIndex > vault.getGroup(groupIndex).size() - 1) {
                printErrorMsg("ERROR: " + entryWord + " is not a valid entry index. Use 'list entries' to show all entry names and numbers.");
                entryIndex = INVALID_INDEX;
            }
        } catch (NumberFormatException e) {
            // Check if the user entered the entry name
            for(int index = 0; index < vault.getGroup(groupIndex).size() - 1; index++) {
                if(entryWord.equals(vault.getGroup(groupIndex).getEntry(index).getName(key, vault.getEncryptionAlgorithm()).toLowerCase())) {
                    entryIndex = index;
                    break;
                }
            }
        }

        // Only continue if entry index is valid
        if(entryIndex != INVALID_INDEX) {
            // Find which type of entry we are editing
            Entry entry = vault.getGroup(groupIndex).getEntry(entryIndex);

            if(entry.isLogin()) {
                editLogin((Login)entry, fieldWord);
            } else if(entry.isPaymentCard()) {
                editPaymentCard((PaymentCard)entry, fieldWord);
            } else if(entry.isSSHKey()) {
                editSSHKey((SSHKey)entry, fieldWord);
            } else if(entry.isSecureNote()) {
                editSecureNote((SecureNote)entry, fieldWord);
            } else {
                // Should never happen
                printErrorMsg("ERROR: entry " + entryWord + " is an unknown entry type. Please report this issue at jgp9201@gmail.com.");
            }
        }
    }

    // Edit a Login in the vault
    private void editLogin(Login login, String field) {
        switch(field) {
            case "name":
                System.out.print("Name: ");
                String name = scanner.nextLine();
                login.setName(name, key, vault.getEncryptionAlgorithm());
                break;

            case "username":
                System.out.print("Username: ");
                String username = scanner.nextLine();
                login.setUsername(username, key, vault.getEncryptionAlgorithm());
                break;

            case "password":
                String password = new String(console.readPassword("Password: "));
                String verifyPassword = new String(console.readPassword("Verify Password: "));

                if(password.equals(verifyPassword)) {
                    login.setPassword(password, key, vault.getEncryptionAlgorithm());
                } else {
                    System.out.println("ERROR: password do not match. Please try again.");
                }
                break;

            case "url":
                System.out.print("URL: ");
                String url = scanner.nextLine();
                login.setUrl(url, key, vault.getEncryptionAlgorithm());
                break;

            case "note":
            case "notes":
                System.out.print("Notes: ");
                String notes = scanner.nextLine();
                login.setNotes(notes, key, vault.getEncryptionAlgorithm());
                break;

            default:
                printErrorMsg("ERROR: " + field + " is not a valid element of a login to edit.");
                break;
        }

        vault.write();
    }

    // Edit a payment card entry
    private void editPaymentCard(PaymentCard paymentCard, String field) {
        switch(field) {
            case "name":
                System.out.print("Name: ");
                String name = scanner.nextLine();
                paymentCard.setName(name, key, vault.getEncryptionAlgorithm());
                break;

            case "cardholdername":
            case "cardholder name":
                System.out.print("Cardholder name: ");
                String cardholderName = scanner.nextLine();
                paymentCard.setCardholderName(cardholderName, key, vault.getEncryptionAlgorithm());
                break;

            case "cardnumber":
            case "card number":
                System.out.print("Card number: ");
                String cardNumber = scanner.nextLine();
                paymentCard.setCardNumber(cardNumber, key, vault.getEncryptionAlgorithm());
                break;

            case "brand":
                System.out.print("Brand: ");
                String brand = scanner.nextLine();
                paymentCard.setBrand(brand, key, vault.getEncryptionAlgorithm());
                break;

            case "expiredate":
            case "expire date":
            case "expirationdate":
            case "expiration date":
                System.out.print("Expiration Date: ");
                String expireDate = scanner.nextLine();
                paymentCard.setExpireDate(expireDate, key, vault.getEncryptionAlgorithm());
                break;

            case "securitycode":
            case "security code":
                System.out.print("Security Code: ");
                String securityCode = scanner.nextLine();
                paymentCard.setSecurityCode(securityCode, key, vault.getEncryptionAlgorithm());
                break;

            case "note":
            case "notes":
                System.out.print("Notes: ");
                String notes = scanner.nextLine();
                paymentCard.setNotes(notes, key, vault.getEncryptionAlgorithm());
                break;

            default:
                printErrorMsg("ERROR: " + field + " is not a valid element of a payment card to edit.");
                break;
        }

        vault.write();
    }

    // Edit an SSH key entry
    private void editSSHKey(SSHKey sshKey, String field) {
        switch(field) {
            case "name":
                System.out.print("Name: ");
                String name = scanner.nextLine();
                sshKey.setName(name, key, vault.getEncryptionAlgorithm());
                break;

            case "privatekey":
            case "private key":
                System.out.print("Private Key: ");
                String privateKey = scanner.nextLine();
                sshKey.setPrivateKey(privateKey, key, vault.getEncryptionAlgorithm());
                break;

            case "pubickey":
            case "public key":
                System.out.print("Public Key: ");
                String publicKey = scanner.nextLine();
                sshKey.setPublicKey(publicKey, key, vault.getEncryptionAlgorithm());
                break;

            case "fingerprint":
                System.out.print("Fingerprint: ");
                String fingerprint = scanner.nextLine();
                sshKey.setFingerprint(fingerprint, key, vault.getEncryptionAlgorithm());
                break;

            case "note":
            case "notes":
                System.out.print("Notes: ");
                String notes = scanner.nextLine();
                sshKey.setNotes(notes, key, vault.getEncryptionAlgorithm());
                break;

            default:
                printErrorMsg("ERROR: " + field + " is not a valid element of an SSH key to edit.");
        }

        vault.write();
    }

    // Edit a secure note entry
    private void editSecureNote(SecureNote secureNote, String field) {
        switch(field) {
            case "name":
                System.out.print("Name: ");
                String name = scanner.nextLine();
                secureNote.setName(name, key, vault.getEncryptionAlgorithm());
                break;

            case "note":
            case "notes":
                System.out.print("Notes: ");
                String notes = scanner.nextLine();
                secureNote.setNotes(notes, key, vault.getEncryptionAlgorithm());
                break;

            default:
                printErrorMsg("ERROR: " + field + " is not a valid element of a secure note to edit.");
        }

        vault.write();
    }

    // Move a group to a new index in the vault
    private void moveGroup(String groupWord, String indexWord) {
        int selectedGroupIndex = INVALID_INDEX;
        int newIndex = INVALID_INDEX;

        // Check if the user entered a group number
        try {
            selectedGroupIndex = Integer.parseInt(groupWord) - 1;

            // Check that this is a valid group index
            if(selectedGroupIndex < 0 || selectedGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group number. Use 'list groups' to show all group names and numbers.");
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the group name
            for(int index = 0; index < vault.size(); index++) {
                if(groupWord.equals(vault.getGroup(index).getName(key, vault.getEncryptionAlgorithm()).toLowerCase())) {
                    selectedGroupIndex = groupIndex;
                    break;
                }
            }

            // Check if the group name was found
            if(selectedGroupIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group name. Use 'list groups' to show all group names and numbers.");
            }
        }

        // Only continue if a valid group index was entered
        if(selectedGroupIndex != INVALID_INDEX) {
            // Get the new index to move it to
            try {
                newIndex = Integer.parseInt(indexWord);

                // Check that this is a valid group index
                if(newIndex < 0 || newIndex > vault.size()) {
                    printErrorMsg("ERROR: " + indexWord + " is not a valid index to place this group.");
                    newIndex = INVALID_INDEX;
                }
            } catch(NumberFormatException e) {
                printErrorMsg("ERROR: " + indexWord + " is not a valid index to place this group.");
            }

            // Only continue if a valid new index was found
            if(newIndex != INVALID_INDEX) {
                vault.moveGroup(selectedGroupIndex, newIndex);

                // If the move group was the one we are currently in, also change to that group index
                if(selectedGroupIndex == groupIndex) {
                    groupIndex = newIndex;
                }
            }
        }
    }

    // Move an entry from one group to another
    private void moveEntry(String entryWord, String groupWord) {
        int entryIndex = INVALID_INDEX;
        int toGroupIndex = INVALID_INDEX;

        try {
            entryIndex = Integer.parseInt(entryWord) - 1;

            // Check that this is a valid entry index
            if(entryIndex < 0 || entryIndex > vault.getGroup(groupIndex).size() - 1) {
                printErrorMsg("ERROR: " + entryWord + " is not a valid entry number. Use 'list entries' to show all entry names and numbers.");
                entryIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered an entry name
            for(int index = 0; index < vault.getGroup(groupIndex).size(); index++) {
                // REVIEW: need to probably just add the encryption algorithm and maybe even the key to the vault encryptor class to avoid having to pass it everytime
                if(entryWord.equals(vault.getGroup(groupIndex).getEntry(index).getName(key, vault.getEncryptionAlgorithm()).toLowerCase())) {
                    entryIndex = index;
                    break;
                }
            }

            // Check if the name was found
            if(entryIndex == INVALID_INDEX) {
                printErrorMsg("ERROR: " + entryWord + " is not a valid entry name. Use 'list entries' to show all entry names and numbers.");
            }
        }

        try {
            toGroupIndex = Integer.parseInt(groupWord) - 1;

            // Check it is a valid group number
            if(toGroupIndex < 0 || toGroupIndex > vault.size() - 1) {
                printErrorMsg("ERROR: " + groupWord + " is not a valid group number. Use 'list group' to show all group names and numbers.");
                toGroupIndex = INVALID_INDEX;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered a group name
            for(int index = 0; index < vault.size(); index++) {
                if(groupWord.equals(vault.getGroup(index).getName(key, vault.getEncryptionAlgorithm()).toLowerCase())) {
                    toGroupIndex = index;
                    break;
                }
            }
        }

        if(entryIndex != INVALID_INDEX && toGroupIndex != INVALID_INDEX) {
            vault.moveEntry(groupIndex, toGroupIndex, entryIndex);
        }
    }

    // Returns true if the given word is one of the aliases for selecting a group
    private boolean isGroupSelected(String word) {
        return (word.equals("group") || word.equals("groups") || word.equals("g"));
    }

    // Returns true if the given word is one of the aliases for selecting an entry
    private boolean isEntrySelected(String word) {
        return (word.equals("entry") || word.equals("entries") || word.equals("e"));
    }
}
