/*
 * NAME: Entry
 * AUTHOR: J. Pisani
 * DATE: 7/16/25
 *
 * DESCRIPTION: Parent class that holds all information relevant to all entry types and allows them to be grouped in the same array
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javax.crypto.SecretKey;
import java.util.Base64;

import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.VaultEncryptor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
public abstract class Entry {
    // The name of the entry
    protected String name;

    // TODO
    // The salt of the entry
    protected String salt;

    // The notes for the entry
    protected String notes;


    // Empty constructor for Jackson
    Entry() {}

    // Get the name of the entry
    public String getName(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(name, key, encryptionAlgorithm);
    }

    // Set the name of the entry
    public void setName(String name, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.name = VaultEncryptor.encrypt(name, key, encryptionAlgorithm);
    }

    // Get the salt of this entry
    public byte[] getSalt() {
        return Base64.getDecoder().decode(salt);
    }

    // Set the salt of this entry
    public void setSalt(byte[] salt) {
        this.salt = Base64.getEncoder().encodeToString(salt);
    }

    // Get the notes for this entry
    public String getNotes(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(notes, key, encryptionAlgorithm);
    }

    // Set the notes for this entry
    public void setNotes(String notes, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.notes = VaultEncryptor.encrypt(notes, key, encryptionAlgorithm);
    }

    // Returns true if this entry is a Login
    public boolean isLogin() {
        return this instanceof Login;
    }

    // Returns true if this entry is a Payment Card
    public boolean isPaymentCard() {
        return this instanceof PaymentCard;
    }

    // Returns true if this entry is an SSH Key
    public boolean isSSHKey() {
        return this instanceof SSHKey;
    }

    // Returns true if this entry is a Secure Note
    public boolean isSecureNote() {
        return this instanceof SecureNote;
    }

    // Print the details of this entry. Must be implemented per subclass due to differentiating elements.
    public abstract void print();
}
