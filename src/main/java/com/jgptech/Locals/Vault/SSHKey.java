/*
 * NAME: SSHKey
 * AUTHOR: J. Pisani
 * DATE: 7/15/25
 *
 * DESCRIPTION: Class to hold data for an SSH key entry
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.crypto.SecretKey;

import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.VaultEncryptor;

@JsonTypeName("sshKey")
class SSHKey extends Entry {
    // The private SSH key
    private String privateKey;

    // The public SSH key
    private String publicKey;

    // The fingerprint of the key
    private String fingerprint;


    // Constructor for a new SSH Key
    SSHKey(String name, String privateKey, String publicKey, String fingerprint, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.name = VaultEncryptor.encrypt(name, key, encryptionAlgorithm);
        this.privateKey = VaultEncryptor.encrypt(privateKey, key, encryptionAlgorithm);
        this.publicKey = VaultEncryptor.encrypt(publicKey, key, encryptionAlgorithm);
        this.fingerprint = VaultEncryptor.encrypt(fingerprint, key, encryptionAlgorithm);
        this.notes = VaultEncryptor.encrypt(notes, key, encryptionAlgorithm);
    }

    // Get the private key
    String getPrivateKey(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(privateKey, key, encryptionAlgorithm);
    }

    // Set the private key
    void setPrivateKey(String privateKey, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.privateKey = VaultEncryptor.encrypt(privateKey, key, encryptionAlgorithm);
    }

    // Get the public key
    String getPublicKey(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(publicKey, key, encryptionAlgorithm);
    }

    // Set the public key
    void setPublicKey(String publicKey, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.publicKey = VaultEncryptor.encrypt(publicKey, key, encryptionAlgorithm);
    }

    // Get the fingerprint
    String getFingerprint(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(fingerprint, key, encryptionAlgorithm);
    }

    // Set the fingerprint
    void setFingerprint(String fingerprint, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.fingerprint = VaultEncryptor.encrypt(fingerprint, key, encryptionAlgorithm);
    }
}
