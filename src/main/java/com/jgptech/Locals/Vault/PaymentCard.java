/*
 * NAME: PaymentCard
 * AUTHOR: J. Pisani
 * DATE: 7/16/25
 *
 * DESCRIPTION: Class to hold data for a payment card entry
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonTypeName;

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


    // Empty constructor for Jackson
    PaymentCard() {}

    // Constructor for a new payment card
    public PaymentCard(String name, String cardholderName, String cardNumber, String brand, String expireDate, String securityCode, String notes, byte[] key) {
        this.name = VaultEncryptor.encrypt(name, key);
        this.cardholderName = VaultEncryptor.encrypt(cardholderName, key);
        this.cardNumber = VaultEncryptor.encrypt( cardNumber, key);
        this.brand = VaultEncryptor.encrypt(brand, key);
        this.expireDate = VaultEncryptor.encrypt(expireDate, key);
        this.securityCode = VaultEncryptor.encrypt(securityCode, key);
        this.notes = VaultEncryptor.encrypt(notes, key);
    }

    // Get the cardholder name
    public String getCardholderName(byte[] key) {
        return VaultEncryptor.decrypt(cardholderName, key);
    }

    // Set the cardholder name
    public void setCardholderName(String cardholderName, byte[] key) {
        this.cardholderName = VaultEncryptor.encrypt(cardholderName, key);
    }

    // Get the card number
    public String getCardNumber(byte[] key) {
        return VaultEncryptor.decrypt(cardNumber, key);
    }

    // Set the card number
    public void setCardNumber(String cardNumber, byte[] key) {
        this.cardNumber = VaultEncryptor.encrypt(cardNumber, key);
    }

    // Get the brand
    public String getBrand(byte[] key) {
        return VaultEncryptor.decrypt(brand, key);
    }

    // Set the brand
    public void setBrand(String brand, byte[] key) {
        this.brand = VaultEncryptor.encrypt(brand, key);
    }

    // Get the expiration date
    public String getExpireDate(byte[] key) {
        return VaultEncryptor.decrypt(expireDate, key);
    }

    // Set the expiration date
    public void setExpireDate(String expireDate, byte[] key) {
        this.expireDate = VaultEncryptor.encrypt(expireDate, key);
    }

    // Get the security code
    public String getSecurityCode(byte[] key) {
        return VaultEncryptor.decrypt(securityCode, key);
    }

    // Set the security code
    public void setSecurityCode(String securityCode, byte[] key) {
        this.securityCode = VaultEncryptor.encrypt(securityCode, key);
    }

    // Print the relevant details for this payment card
    public void print(byte[] key) {
        System.out.println();
        System.out.println("Name: " + getName(key));
        System.out.println("Cardholder Name: " + getCardholderName(key));
        System.out.println("Card Number: " + getCardNumber(key)); // REVIEW: add way to display without showing card number, like '**** **** **** ****'
        System.out.println("Brand: " + getBrand(key));
        System.out.println("Expiration Date: " + getExpireDate(key));
        System.out.println("Security Code: " + getSecurityCode(key));
        System.out.println("Notes: " + getNotes(key));
        System.out.println();
    }
}
