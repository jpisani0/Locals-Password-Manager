/*
 * NAME: Vault
 * AUTHOR: J. Pisani
 * DATE: 4/24/25
 *
 * DESCRIPTION: Class to organize and manipulate the vault file data in memory
 */

package Vault;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.nio.file.*;
import java.io.IOException;

public class Vault {
    private Path path = null; // Path object of the vault
    private String salt = ""; // The salt for this vault
    private String masterHash = ""; // Hash of the master password for this vault
    private ArrayList<Group> groups = new ArrayList<>(); // Groups in the vault

    // Constructor for creating a new vault
    public Vault(String filename, String salt, String masterHash) {
        this.path = Paths.get(filename);
        this.salt = salt;
        this.masterHash = masterHash;
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
    public void addGroup(@NotNull Group group) {
        groups.add(group);
    }

    // Add a group at a specific index of this vault
    public boolean addGroup(@NotNull Group group, int groupIndex) {
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
                System.out.println(group.getGroupName());
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
