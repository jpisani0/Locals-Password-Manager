/*
 * NAME: Vault
 * AUTHOR: J. Pisani
 * DATE: 4/24/25
 *
 * DESCRIPTION: Class to organize and manipulate the vault file data in memory
 */

package Vault;

import java.util.ArrayList;
import java.nio.file.*;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Vault {
    private Path path = null; // Path object of the vault

    @JsonProperty("hashAlg")
    private String hashingAlgorithm = ""; // Hashing algorithm used for this vault // TODO: change to HashingAlgorithm enum

    @JsonProperty("encrAlg")
    private String encryptionAlgorithm = ""; // Encryption algorithm used for this vault // TODO: change to EncryptionAlgorithm enum

    @JsonProperty("iter")
    private int iterations; // Iterations for the hashing algorithm

    private String salt = ""; // The salt for this vault

    private String masterHash = ""; // Hash of the master password for this vault

    private ArrayList<Group> groups = new ArrayList<>(); // Groups in the vault


    // Constructor for loading an existing vault (Jackson requires an empty constructor)
    public Vault() {}

    // Constructor for creating a new vault
    public Vault(String filename, String hashingAlgorithm, String encryptionAlgorithm, int iterations, String salt, String masterHash) {
        this.path = Paths.get(filename);
        this.hashingAlgorithm = hashingAlgorithm;
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.iterations = iterations;
        this.salt = salt;
        this.masterHash = masterHash;
    }

    // TODO: enum
    // Get the hashing algorithm for this vault
    public String getHashingAlgorithm() {
        return this.hashingAlgorithm;
    }

    // TODO: enum
    // Set the hashing algorithm for this vault
    public void setHashingAlgorithm(String hashingAlgorithm) {
        this.hashingAlgorithm = hashingAlgorithm;
    }

    // TODO: enum
    // Get the encryption algorithm for this vault
    public String getEncryptionAlgorithm() {
        return this.encryptionAlgorithm;
    }

    // TODO: enum
    // Set the encryption algorithm for this vault
    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
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
    public boolean load() throws IOException {
        boolean success = true;

        return success;
    }

    // Write data to the vault file
    public boolean write() throws IOException {
        boolean success = true;

        // TODO: need to handle trying to create a new password file vs updating an existing one
        // Try to create the file
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            throw new IOException("File " + path.toString() + " already exists!", e);
        } catch (IOException e) {
            throw new IOException("Could not create file " + path.toString(), e);
        }

        return success;
    }
}
