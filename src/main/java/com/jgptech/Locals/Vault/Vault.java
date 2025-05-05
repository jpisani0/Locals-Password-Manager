/*
 * NAME: com.jgptech.Locals.Vault
 * AUTHOR: J. Pisani
 * DATE: 4/24/25
 *
 * DESCRIPTION: Class to organize and manipulate the vault file data in memory
 */

package com.jgptech.Locals.Vault;

import java.util.ArrayList;
import java.nio.file.*;
import java.io.IOException;

import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.HashingAlgorithm;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Vault {
    // Path object of the vault
    private Path path = null;

    // Hashing algorithm used for this vault
    private HashingAlgorithm hashingAlgorithm;

    // com.jgptech.Locals.Encryption algorithm used for this vault
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
    public Vault(String filename, HashingAlgorithm hashingAlgorithm, EncryptionAlgorithm encryptionAlgorithm, int iterations, String salt, String masterHash) {
        this.path = Paths.get(filename);
        this.hashingAlgorithm = hashingAlgorithm;
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.iterations = iterations;
        this.salt = salt;
        this.masterHash = masterHash;
    }

    // Get the hashing algorithm for this vault
    public HashingAlgorithm getHashingAlgorithm() {
        return this.hashingAlgorithm;
    }

    // Set the hashing algorithm for this vault
    public void setHashingAlgorithm(HashingAlgorithm hashingAlgorithm) {
        this.hashingAlgorithm = hashingAlgorithm;
    }

    // Get the encryption algorithm for this vault
    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return this.encryptionAlgorithm;
    }

    // Set the encryption algorithm for this vault
    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    // Get the number of iterations for this vault
    public int getIterations() {
        return this.iterations;
    }

    // Set the number of iterations for this vault
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    // Get the salt for this vault
    public String getSalt() {
        return this.salt;
    }

    // Set the salt for this vault
    public void setSalt(String salt) {
        this.salt = salt;
    }

    // Get the hash of the master password for this vault
    public String getMasterHash() {
        return this.masterHash;
    }

    // Set the hash of the master password for this vault
    public void setMasterHash(String masterHash) {
        this.masterHash = masterHash;
    }

    // Get a group in this vault
    public Group getGroup(int groupIndex) {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            return null;
        }

        return groups.get(groupIndex);
    }

    // Add a group to the end of this vault
    public void addGroup(Group group) {
        groups.add(group);
    }

    // Add a group at a specific index of this vault
    public boolean addGroup(Group group, int groupIndex) {
        if(groupIndex < 0 || groupIndex > groups.size()) {
            return false;
        }

        groups.add(groupIndex, group);
        return true;
    }

    // List all the groups in this vault
    public void listGroups() {
        if(!groups.isEmpty()) {
            for(Group group : groups) {
                System.out.println(group.getName());
            }
        }
    }

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

        // TODO
        // Check that this vault has enough data to write


        // REVIEW: not checking if file already exists because if it does, we assume that we are updating an existing file. Correct?
        // Check if the file path exists
        if(Files.exists(path)) {
            // Check if the file path is a directory
            if(Files.isDirectory(path)) {
                System.out.println("Error: path the vault is a directory: " + path.toString());
                success = false;
            }
        }

        // Try to create the file
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), this);
        } catch (IOException e) {
            System.out.println("Error: could not write data to file: " + e.getMessage());
        }

        return success;
    }
}
