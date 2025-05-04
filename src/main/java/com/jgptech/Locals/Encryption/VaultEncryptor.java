/*
 * NAME: VaultEncryptor
 * AUTHOR: J. Pisani
 * DATE: 4/26/25
 *
 * DESCRIPTION: Support for encrypting/decrypting vault data
 */

package com.jgptech.Locals.Encryption;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class VaultEncryptor {
    public SecretKey key; // Key for encrypting/decrypting the data
    public EncryptionAlgorithm algorithm; // com.jgptech.Locals.Encryption algorithm for this encryptor

    // Construct for the VaultEncryption class
    public VaultEncryptor(SecretKey key, EncryptionAlgorithm algorithm) {
        this.key = key;
        this.algorithm = algorithm;
    }

    // TODO: add support for other encryption algorithms, likely using a private method that converts the EncryptionAlgorithm enum to the string for Cipher

    // Encrypt the data to be stored in the vault
    public String encrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String algorithmName = "AES";
        Cipher cipher = Cipher.getInstance(algorithmName);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData); // Encode with Base64 here as the resulting bytes from encryption may not correspond to actual characters
    }

    // Decrypt the data from the vault
    public String decrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String algorithmName = "AES";
        Cipher cipher = Cipher.getInstance(algorithmName);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(decryptedData); // Return as a string cast here as we know the decrypted data will be able to be represented this way
    }
}
