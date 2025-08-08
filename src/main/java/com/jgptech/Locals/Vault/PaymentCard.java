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
public class PaymentCard extends Entry {
    // Name on the card
    private String cardholderName;

    // The card number
    private String cardNumber;

    // The brand of the card
    private String brand; // TODO: add enum for brands

    // The expiration date of the card
    private String expireDate;

    // The security code of the card
    private String securityCode;


    // Constructor for a new payment card
    public PaymentCard(String name, String cardholderName, String cardNumber, String brand, String expireDate, String securityCode, String notes, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.name = VaultEncryptor.encrypt(name, key, encryptionAlgorithm);
        this.cardholderName = VaultEncryptor.encrypt(cardholderName, key, encryptionAlgorithm);
        this.cardNumber = VaultEncryptor.encrypt( cardNumber, key, encryptionAlgorithm);
        this.brand = VaultEncryptor.encrypt(brand, key, encryptionAlgorithm);
        this.expireDate = VaultEncryptor.encrypt(expireDate, key, encryptionAlgorithm);
        this.securityCode = VaultEncryptor.encrypt(securityCode, key, encryptionAlgorithm);
        this.notes = VaultEncryptor.encrypt(notes, key, encryptionAlgorithm);
    }

    // Get the cardholder name
    public String getCardholderName(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(cardholderName, key, encryptionAlgorithm);
    }

    // Set the cardholder name
    public void setCardholderName(String cardholderName, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.cardholderName = VaultEncryptor.encrypt(cardholderName, key, encryptionAlgorithm);
    }

    // Get the card number
    public String getCardNumber(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(cardNumber, key, encryptionAlgorithm);
    }

    // Set the card number
    public void setCardNumber(String cardNumber, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.cardNumber = VaultEncryptor.encrypt(cardNumber, key, encryptionAlgorithm);
    }

    // Get the brand
    public String getBrand(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(brand, key, encryptionAlgorithm);
    }

    // Set the brand
    public void setBrand(String brand, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.brand = VaultEncryptor.encrypt(brand, key, encryptionAlgorithm);
    }

    // Get the expiration date
    public String getExpireDate(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(expireDate, key, encryptionAlgorithm);
    }

    // Set the expiration date
    public void setExpireDate(String expireDate, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.expireDate = VaultEncryptor.encrypt(expireDate, key, encryptionAlgorithm);
    }

    // Get the security code
    public String getSecurityCode(SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        return VaultEncryptor.decrypt(securityCode, key, encryptionAlgorithm);
    }

    // Set the security code
    public void setSecurityCode(String securityCode, SecretKey key, EncryptionAlgorithm encryptionAlgorithm) {
        this.securityCode = VaultEncryptor.encrypt(securityCode, key, encryptionAlgorithm);
    }

    // Print the relevant details for this payment card
    public void print() {
        System.out.println();
        System.out.println("Name: " + name);
        System.out.println("Cardholder Name: " + cardholderName);
        System.out.println("Card Number: " + cardNumber); // REVIEW: add way to display without showing card number, like '**** **** **** ****'
        System.out.println("Brand: " + brand);
        System.out.println("Expiration Date: " + expireDate);
        System.out.println("Security Code: " + securityCode);
        System.out.println("Notes: " + notes);
        System.out.println();
    }
}
