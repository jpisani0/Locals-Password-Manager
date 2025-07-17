/*
 * NAME: PaymentCard
 * AUTHOR: J. Pisani
 * DATE: 7/16/25
 *
 * DESCRIPTION: Class to hold data for a payment card entry
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.crypto.SecretKey;

import com.jgptech.Locals.Encryption.EncryptionAlgorithm;
import com.jgptech.Locals.Encryption.VaultEncryptor;

@JsonTypeName("paymentCard")
class PaymentCard extends Entry {
    // Name on the card
    private String cardholderName;

    // The card number
    private String cardNumber;

    // The brand of the card
    private String brand;

    // The expiration date of the card
    private String expireDate;

    // The security code of the card
    private String securityCode;


    // Constructor for a new payment card
    PaymentCard(String name, String cardholderName, String cardNumber, String brand, String expireDate, String securityCode, String notes, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.name = VaultEncryptor.encrypt(name, key, encryptionAlgorithm);
        this.cardholderName = VaultEncryptor.encrypt(cardholderName, key, encryptionAlgorithm);
        this.cardNumber = VaultEncryptor.encrypt( cardNumber, key, encryptionAlgorithm);
        this.brand = VaultEncryptor.encrypt(brand, key, encryptionAlgorithm);
        this.expireDate = VaultEncryptor.encrypt(expireDate, key, encryptionAlgorithm);
        this.securityCode = VaultEncryptor.encrypt(securityCode, key, encryptionAlgorithm);
        this.notes = VaultEncryptor.encrypt(notes, key, encryptionAlgorithm);
    }

    // Get the cardholder name
    String getCardholderName(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(cardholderName, key, encryptionAlgorithm);
    }

    // Set the cardholder name
    void setCardholderName(String cardholderName, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.cardholderName = VaultEncryptor.encrypt(cardholderName, key, encryptionAlgorithm);
    }

    // Get the card number
    String getCardNumber(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(cardNumber, key, encryptionAlgorithm);
    }

    // Set the card number
    void setCardNumber(String cardNumber, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.cardNumber = VaultEncryptor.encrypt(cardNumber, key, encryptionAlgorithm);
    }

    // Get the brand
    String getBrand(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(brand, key, encryptionAlgorithm);
    }

    // Set the brand
    void setBrand(String brand, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.brand = VaultEncryptor.encrypt(brand, key, encryptionAlgorithm);
    }

    // Get the expiration date
    String getExpireDate(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(expireDate, key, encryptionAlgorithm);
    }

    // Set the expiration date
    void setExpireDate(String expireDate, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.expireDate = VaultEncryptor.encrypt(expireDate, key, encryptionAlgorithm);
    }

    // Get the security code
    String getSecurityCode(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(securityCode, key, encryptionAlgorithm);
    }

    // Set the security code
    void setSecurityCode(String securityCode, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.securityCode = VaultEncryptor.encrypt(securityCode, key, encryptionAlgorithm);
    }
}
