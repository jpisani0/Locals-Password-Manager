/*
 * NAME: Entry
 * AUTHOR: J. Pisani
 * DATE: 7/16/25
 *
 * DESCRIPTION: Parent class that holds all information relevant to all entry types and allows them to be grouped in the same array
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Base64;

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
    // The initialization vector of the entry. Base64 encoded.
    protected String iv;

    // The notes for the entry
    protected String notes;


    // Empty constructor for Jackson
    Entry() {}

    // Get the name of the entry
    public String getName(byte[] key) {
        return VaultEncryptor.decrypt(name, key, getIV());
    }

    // Set the name of the entry
    public void setName(String name, byte[] key) {
        this.name = VaultEncryptor.encrypt(name, key, getIV());
    }

    // Get the initialization vector of this entry
    public byte[] getIV() {
        return Base64.getDecoder().decode(iv);
    }

    // Set the initialization vector of this entry
    public void setIV(byte[] iv) {
        this.iv = Base64.getEncoder().encodeToString(iv);
    }

    // Get the notes for this entry
    public String getNotes(byte[] key) {
        return VaultEncryptor.decrypt(notes, key, getIV());
    }

    // Set the notes for this entry
    public void setNotes(String notes, byte[] key) {
        this.notes = VaultEncryptor.encrypt(notes, key, getIV());
    }

    @JsonIgnore
    // Returns true if this entry is a Login
    public boolean isLogin() {
        return this instanceof Login;
    }

    @JsonIgnore
    // Returns true if this entry is a Payment Card
    public boolean isPaymentCard() {
        return this instanceof PaymentCard;
    }

    @JsonIgnore
    // Returns true if this entry is an SSH Key
    public boolean isSSHKey() {
        return this instanceof SSHKey;
    }

    @JsonIgnore
    // Returns true if this entry is a Secure Note
    public boolean isSecureNote() {
        return this instanceof SecureNote;
    }

    @JsonIgnore
    // Print the details of this entry. Must be implemented per subclass due to differentiating elements.
    public abstract void print();
}
