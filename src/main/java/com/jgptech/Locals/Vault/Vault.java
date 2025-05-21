/*
 * NAME: com.jgptech.Locals.Vault
 * AUTHOR: J. Pisani
 * DATE: 4/24/25
 *
 * DESCRIPTION: Class to organize and manipulate the vault file data in memory
 */

package com.jgptech.Locals.Vault;

import java.awt.*;
import java.util.ArrayList;
import java.nio.file.*;
import java.io.IOException;
import java.util.Base64;
import javax.crypto.*;

import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.HashingAlgorithm;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Vault {
    // Path object of the vault
    private Path path = null;

    // Hashing algorithm used for this vault
    private HashingAlgorithm hashingAlgorithm;

    // Encryption algorithm used for this vault
    private EncryptionAlgorithm encryptionAlgorithm;

    // Iterations for the hashing algorithm
    private int iterations;

    // The salt for this vault
    private String salt;

    // Hash of the master password for this vault
    private String masterHash;

    // Groups in the vault
    private ArrayList<Group> groups = new ArrayList<>();


    // Constructor for loading an existing vault (Jackson requires an empty constructor)
    public Vault() {}

    // Constructor for creating a new vault
    public Vault(String filename, HashingAlgorithm hashingAlgorithm, EncryptionAlgorithm encryptionAlgorithm, int iterations, byte[] salt, byte[] masterHash, SecretKey key) {
        this.path = Paths.get(filename);
        this.hashingAlgorithm = hashingAlgorithm;
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.iterations = iterations;
        this.salt = Base64.getEncoder().encodeToString(salt);
        this.masterHash = Base64.getEncoder().encodeToString(masterHash);
        groups.add(new Group("General", Color.blue, key, encryptionAlgorithm));
    }

    // Get the name of the vault
    public String getName() {
        return path.toString();
    }

    // Get the hashing algorithm for this vault
    public HashingAlgorithm getHashingAlgorithm() {
        return hashingAlgorithm;
    }

    // Set the hashing algorithm for this vault
    public void setHashingAlgorithm(HashingAlgorithm hashingAlgorithm) {
        this.hashingAlgorithm = hashingAlgorithm;
    }

    // Get the encryption algorithm for this vault
    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    // Set the encryption algorithm for this vault
    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    // Get the number of iterations for this vault
    public int getIterations() {
        return iterations;
    }

    // Set the number of iterations for this vault
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    // Get the salt for this vault
    public byte[] getSalt() {
        return Base64.getDecoder().decode(salt);
    }

    // Set the salt for this vault
    public void setSalt(byte[] salt) {
        this.salt = Base64.getEncoder().encodeToString(salt);
    }

    // Get the hash of the master password for this vault
    public byte[] getMasterHash() {
        return Base64.getDecoder().decode(masterHash);
    }

    // Set the hash of the master password for this vault
    public void setMasterHash(byte[] masterHash) {
        this.masterHash = Base64.getEncoder().encodeToString(masterHash);
    }

    @JsonIgnore
    // Checks if this vault is empty
    public boolean isEmpty() {
        return groups.isEmpty();
    }

    // Load data from the vault file
    public static Vault load(String vaultName) {
        ObjectMapper mapper = new ObjectMapper();
        Path vaultPath = Paths.get(vaultName);

        try {
            return mapper.readValue(vaultPath.toFile(), Vault.class);
        } catch (StreamReadException e) {
            System.out.println("Error: malformed JSON: " + e.getMessage());
        } catch (DatabindException e) {
            System.out.println("Error: JSON formatting incorrect for vault file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: could not read vault file" + e.getMessage());
        }

        return null;
    }

    // Write data to the vault file
    public boolean write() {
        ObjectMapper mapper = new ObjectMapper();
        boolean success = true;

        // Check that this vault has enough data to write
        if(path == null || hashingAlgorithm == HashingAlgorithm.NoHashingAlgorithm ||
                encryptionAlgorithm == EncryptionAlgorithm.NoEncryptionAlgorithm || iterations < 1 || salt.isEmpty()) {
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

    @JsonIgnore
    // Get the name of a group in this vault
    public String getGroupName(int groupIndex, SecretKey key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        return groups.get(groupIndex).getName(key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the name of a group in this vault
    public void setGroupName(int groupIndex, SecretKey key, String name) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        groups.get(groupIndex).setName(name, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Get the color of a group in this vault
    public Color getGroupColor(int groupIndex) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        return groups.get(groupIndex).getColor();
    }

    @JsonIgnore
    // Set the color of a group in this vault
    public void setGroupColor(int groupIndex, Color color) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        groups.get(groupIndex).setColor(color);
    }

    @JsonIgnore
    // Add a group to the end of this vault
    public void addGroup(String name, Color color, SecretKey key) {
        groups.add(new Group(name, color, key, encryptionAlgorithm));
    }

    @JsonIgnore
    // Add a group at a specific index of this vault
    public void addGroup(int groupIndex, String name, Color color, SecretKey key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        groups.add(groupIndex, new Group(name, color, key, encryptionAlgorithm));
    }

    @JsonIgnore
    // List all the groups in this vault
    public void listGroups(SecretKey key) {
        if(!groups.isEmpty()) {
            for(Group group : groups) {
                System.out.println(group.getName(key, encryptionAlgorithm));
            }
        }
    }

    @JsonIgnore
    // Get the name of an entry in this vault
    public String getEntryName(int groupIndex, int entryIndex, SecretKey key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        return groups.get(groupIndex).getEntryName(entryIndex, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the name of an entry in this vault
    public void setEntryName(int groupIndex, int entryIndex, SecretKey key, String name) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        groups.get(groupIndex).setEntryName(entryIndex, key, encryptionAlgorithm, name);
    }

    @JsonIgnore
    // Get the username of an entry in this vault
    public String getEntryUsername(int groupIndex, int entryIndex, SecretKey key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        return groups.get(groupIndex).getEntryUsername(entryIndex, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the username of an entry in this vault
    public void setEntryUsername(int groupIndex, int entryIndex, SecretKey key, String username) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        groups.get(groupIndex).setEntryUsername(entryIndex, key, encryptionAlgorithm, username);
    }

    @JsonIgnore
    // Get the password of an entry in this vault
    public String getEntryPassword(int groupIndex, int entryIndex, SecretKey key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        return groups.get(groupIndex).getEntryPassword(entryIndex, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the password of an entry in this vault
    public void setEntryPassword(int groupIndex, int entryIndex, SecretKey key, String password) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        groups.get(groupIndex).setEntryPassword(entryIndex, key, encryptionAlgorithm, password);
    }

    @JsonIgnore
    // Get the URL of an entry in this vault
    public String getEntryUrl(int groupIndex, int entryIndex, SecretKey key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        return groups.get(groupIndex).getEntryUrl(entryIndex, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the URL of an entry in this vault
    public void setEntryUrl(int groupIndex, int entryIndex, SecretKey key, String url) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        groups.get(groupIndex).setEntryUrl(entryIndex, key, encryptionAlgorithm, url);
    }

    @JsonIgnore
    // Get the notes of an entry in this vault
    public String getEntryNotes(int groupIndex, int entryIndex, SecretKey key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        return groups.get(groupIndex).getEntryNotes(entryIndex, key, encryptionAlgorithm);
    }

    @JsonIgnore
    // Set the notes of an entry in this vault
    public void setEntryNotes(int groupIndex, int entryIndex, SecretKey key, String notes) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        groups.get(groupIndex).setEntryNotes(entryIndex, key, encryptionAlgorithm, notes);
    }

    // List the entries of a group in the vault
    public void listEntries(int groupIndex, SecretKey key) throws IndexOutOfBoundsException {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            throw new IndexOutOfBoundsException("Invalid group index: " + groupIndex);
        }

        groups.get(groupIndex).listEntries(key, encryptionAlgorithm);
    }
}
