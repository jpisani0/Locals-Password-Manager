/*
 * NAME: SSHKey
 * AUTHOR: J. Pisani
 * DATE: 7/15/25
 *
 * DESCRIPTION: Class to hold data for an SSH key entry
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonTypeName;

import com.jgptech.Locals.Encryption.VaultEncryptor;

@JsonTypeName("sshKey")
public class SSHKey extends Entry {
    // The private SSH key
    private String privateKey;

    // The public SSH key
    private String publicKey;

    // The fingerprint of the key
    private String fingerprint;


    // Empty constructor for Jackson
    SSHKey() {}

    // Constructor for a new SSH Key
    public SSHKey(String name, String privateKey, String publicKey, String fingerprint, byte[] key) {
        this.name = VaultEncryptor.encrypt(name, key, getIV());
        this.privateKey = VaultEncryptor.encrypt(privateKey, key, getIV());
        this.publicKey = VaultEncryptor.encrypt(publicKey, key, getIV());
        this.fingerprint = VaultEncryptor.encrypt(fingerprint, key, getIV());
        this.notes = VaultEncryptor.encrypt(notes, key, getIV());
    }

    // Get the private key
    public String getPrivateKey(byte[] key) {
        return VaultEncryptor.decrypt(privateKey, key, getIV());
    }

    // Set the private key
    public void setPrivateKey(String privateKey, byte[] key) {
        this.privateKey = VaultEncryptor.encrypt(privateKey, key, getIV());
    }

    // Get the public key
    public String getPublicKey(byte[] key) {
        return VaultEncryptor.decrypt(publicKey, key, getIV());
    }

    // Set the public key
    public void setPublicKey(String publicKey, byte[] key) {
        this.publicKey = VaultEncryptor.encrypt(publicKey, key, getIV());
    }

    // Get the fingerprint
    public String getFingerprint(byte[] key) {
        return VaultEncryptor.decrypt(fingerprint, key, getIV());
    }

    // Set the fingerprint
    public void setFingerprint(String fingerprint, byte[] key) {
        this.fingerprint = VaultEncryptor.encrypt(fingerprint, key, getIV());
    }

    // Print the relevant details for ssh key
    public void print(byte[] key) {
        System.out.println();
        System.out.println("Name: " + getName(key));
        System.out.println("Private Key: " + getPrivateKey(key)); // REVIEW: add way to hide private key
        System.out.println("Public Key: " + getPublicKey(key));
        System.out.println("Fingerprint: " + getFingerprint(key));
        System.out.println("Notes: " + getNotes(key));
        System.out.println();
    }
}
