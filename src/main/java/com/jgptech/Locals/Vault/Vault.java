/*
 * NAME: com.jgptech.Locals.Vault
 * AUTHOR: J. Pisani
 * DATE: 4/24/25
 *
 * DESCRIPTION: Class to organize and manipulate the vault file data in memory
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.databind.jsontype.NamedType;

import java.awt.*;
import java.util.ArrayList;
import java.nio.file.*;
import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Vault {
    @JsonIgnore
    // Path object of the vault
    private Path path = null;

//    // Hashing algorithm used for this vault
//    private HashingAlgorithm hashingAlgorithm;
//
//    // Encryption algorithm used for this vault
//    private EncryptionAlgorithm encryptionAlgorithm;

    // The salt used with the master password to derive the secret key for encryption/decryption in this vault
    private String saltEnc;

    // The salt used with the master password to output a hash used for authentication purposes
    private String saltAuth;

    // Hash of the master password for this vault
    private String masterHash;

    // Groups in the vault
    private ArrayList<Folder> folders = new ArrayList<>();

    @JsonIgnore
    // The default/general group index
    private final int GENERAL_GROUP_INDEX = 1;


    // Constructor for loading an existing vault (Jackson requires an empty constructor)
    Vault() {}

    // Constructor for creating a new vault
    public Vault(String filename, byte[] key, byte[] saltEnc, byte[] saltAuth, byte[] masterHash) {
        this.path = Paths.get(filename);
        this.saltEnc = Base64.getEncoder().encodeToString(saltEnc);
        this.saltAuth = Base64.getEncoder().encodeToString(saltAuth);
        this.masterHash = Base64.getEncoder().encodeToString(masterHash);
        folders.add(new Folder("General", Color.blue, key));
    }

    @JsonIgnore
    // Get the name of the vault
    public String getName() {
        return path.toString();
    }

    @JsonIgnore
    // Set the name of this vault
    public void setName(String name) {
        path = Paths.get(name);
    }

//    // Get the hashing algorithm for this vault
//    public HashingAlgorithm getHashingAlgorithm() {
//        return hashingAlgorithm;
//    }
//
//    // Set the hashing algorithm for this vault
//    public void setHashingAlgorithm(HashingAlgorithm hashingAlgorithm) {
//        this.hashingAlgorithm = hashingAlgorithm;
//    }
//
//    // Get the encryption algorithm for this vault
//    public EncryptionAlgorithm getEncryptionAlgorithm() {
//        return encryptionAlgorithm;
//    }
//
//    // Set the encryption algorithm for this vault
//    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
//        this.encryptionAlgorithm = encryptionAlgorithm;
//    }

    // Get the saltEnc for this vault
    public byte[] getSaltEnc() {
        return Base64.getDecoder().decode(saltEnc);
    }

    // Set the saltEnc for this vault
    public void setSaltEnc(byte[] saltEnc) {
        this.saltEnc = Base64.getEncoder().encodeToString(saltEnc);
    }

    // Get the saltAuth for this vault
    public byte[] getSaltAuth() {
        return Base64.getDecoder().decode(saltAuth);
    }

    // Set the saltAuth for this vault
    public void setSaltAuth(byte[] saltAuth) {
        this.saltAuth = Base64.getEncoder().encodeToString(saltAuth);
    }

    // Get the hash of the master password for this vault
    public byte[] getMasterHash() {
        return Base64.getDecoder().decode(masterHash);
    }

    // Set the hash of the master password for this vault
    public void setMasterHash(byte[] masterHash) {
        this.masterHash = Base64.getEncoder().encodeToString(masterHash);
    }

    // Get the amount of groups in this vault
    public int size() {
        return folders.size();
    }

    @JsonIgnore
    // Checks if this vault is empty
    public boolean isEmpty() {
        return folders.isEmpty();
    }

    // Load data from the vault file
    public static Vault load(String vaultName) {
        ObjectMapper mapper = new ObjectMapper();

        // Register the Entry subclasses
        mapper.registerSubtypes(
                new NamedType(Login.class, "login"),
                new NamedType(PaymentCard.class, "paymentCard"),
                new NamedType(SecureNote.class, "secureNote"),
                new NamedType(SSHKey.class, "sshKey")
        );

        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Path vaultPath = Paths.get(vaultName);

        try {
            return mapper.readValue(vaultPath.toFile(), Vault.class);
        } catch (StreamReadException e) {
            System.out.println("Error: malformed JSON: " + e.getMessage());
        } catch (DatabindException e) {
            System.out.println("Error: JSON formatting incorrect for vault file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: could not read vault file: " + e.getMessage());
        }

        return null;
    }

    // Write data to the vault file
    public boolean write() {
        ObjectMapper mapper = new ObjectMapper();

        // Register the Entry subclasses
        mapper.registerSubtypes(
                new NamedType(Login.class, "login"),
                new NamedType(PaymentCard.class, "paymentCard"),
                new NamedType(SecureNote.class, "secureNote"),
                new NamedType(SSHKey.class, "sshKey")
        );

        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        boolean success = true;

        // Check that this vault has enough data to write
        if(path == null || saltEnc.isEmpty() || saltAuth.isEmpty()) {
            System.out.println("ERROR: not enough data to write to vault");
            success = false;
        } else {
            // REVIEW: not checking if file already exists because if it does, we assume that we are updating an existing file. Correct?
            // Check if the file path exists
            if(Files.exists(path)) {
                // Check if the file path is a directory
                if(Files.isDirectory(path)) {
                    System.out.println("Error: vault path is a directory: " + path.toString());
                    success = false;
                }
            }

            // Try to create the file
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), this);
            } catch (IOException e) {
                System.out.println("Error: could not write data to file: " + e.getMessage());
                success = false;
            }
        }

        return success;
    }

    /****************************************************************************************************************/
    /***************************************************** GROUP ****************************************************/
    /****************************************************************************************************************/

    // REVIEW: can maybe remove these two
    @JsonProperty
    // Get the groups array (for Jackson)
    ArrayList<Folder> getGroups() {
        return folders;
    }

    @JsonProperty
    // Set the groups array (for Jackson)
    void setGroups(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    @JsonIgnore
    public Folder getGroup(int groupIndex) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > folders.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        return folders.get(groupIndex);
    }

    @JsonIgnore
    // Add a group to the end of this vault
    public void addGroup(Folder folder) {
        folders.add(folder);
    }

    @JsonIgnore
    // Add a group at a specific index of this vault
    public void addGroup(int groupIndex, String name, Color color, byte[] key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > folders.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        folders.add(groupIndex, new Folder(name, color, key));
    }

    @JsonIgnore
    // Move a group in the array
    public void moveGroup(int currentGroupIndex, int newGroupIndex) throws IndexOutOfBoundsException {
        if(currentGroupIndex < 0 || currentGroupIndex > folders.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + currentGroupIndex);
        } else if(newGroupIndex < 0 || newGroupIndex > folders.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + newGroupIndex);
        }

        Folder folder = folders.get(currentGroupIndex);
        folders.remove(currentGroupIndex);

        if(newGroupIndex == folders.size() - 1) {
            folders.add(folder);
        } else {
            folders.add(newGroupIndex, folder);
        }
    }

    public void removeGroup(int groupIndex) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > folders.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        } else if(groupIndex == 1) {
            throw new IndexOutOfBoundsException("Cannot remove 'General' group"); // REVIEW: general group should later be made to "All" and contain references to all entries in all groups
        }

        // Take all entries out of this group and place them in the General/Default group
        for(int entryIndex = 0; entryIndex < folders.get(groupIndex).size(); entryIndex++) {
            Entry entry = folders.get(groupIndex).getEntry(entryIndex);

            // Add entry to the General group
            folders.get(GENERAL_GROUP_INDEX).addEntry(entry);

            // Remove the entry from the previous group
            folders.get(groupIndex).removeEntry(entryIndex);
        }

        // We can now safely remove the group with no entries in it
        folders.remove(groupIndex);
    }

    @JsonIgnore
    // List all the groups in this vault
    public void listGroups(byte[] key) {
        if(!folders.isEmpty()) {
            System.out.println();

            for(int index = 0; index < folders.size(); index++) {
                System.out.println((index + 1) + ". " + folders.get(index).getName(key));
            }

            System.out.println();
        }
    }

    // Check if a given group index is valid for this group
    public int isValidGroupIndex(String groupWord, byte[] key) {
        int groupIndex = -1;

        // Check if the user entered the group number
        try {
            groupIndex = Integer.parseInt(groupWord) - 1;

            // Check if this is a valid group index
            if(groupIndex < 0 || groupIndex > size() - 1) {
                groupIndex = -1;
            }
        } catch(NumberFormatException e) {
            // Check if the user entered the group name
            for(int index = 0; index < size(); index++) {
                if(groupWord.equals(getGroup(index).getName(key).toLowerCase())) {
                    groupIndex = index;
                    break;
                }
            }
        }

        return groupIndex;
    }

    /****************************************************************************************************************/
    /***************************************************** ENTRY ****************************************************/
    /****************************************************************************************************************/

    // Move an entry from one group to another
    public void moveEntry(int fromGroupIndex, int toGroupIndex, int entryIndex) throws IndexOutOfBoundsException {
        if(fromGroupIndex < 0 || fromGroupIndex > folders.size()) {
            throw new IndexOutOfBoundsException("Invalid from group index: " + fromGroupIndex);
        }

        if(toGroupIndex < 0 || toGroupIndex > folders.size()) {
            throw new IndexOutOfBoundsException("Invalid to group index:" + toGroupIndex);
        }

        Entry entry = folders.get(fromGroupIndex).getEntry(entryIndex);
        folders.get(toGroupIndex).addEntry(entry);
        folders.get(fromGroupIndex).removeEntry(entryIndex);
    }

    // List the entries of a group in the vault
    public void listEntries(int groupIndex, byte[] key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > folders.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        folders.get(groupIndex).listEntries(key);
    }
}
