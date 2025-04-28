/*
 * NAME: PasswordGenerator
 * AUTHOR: J. Pisani
 * DATE: 4/27/25
 *
 * DESCRIPTION: Generate a secure password for the user if they don't want to make one themselves
 */

package com.jgptech.locals.Encryption;

import java.security.SecureRandom;

public abstract class PasswordGenerator {
    // Strings that hold all possible characters for the password
    private static final String LOWERCASE_LETTERS = "qwertyuiopasdfghjklzxcvbnm";
    private static final String UPPERCASE_LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String DIGITS = "1234567890";
    private static final String SYMBOLS = "`~!@#$%^&*()-_=+[{]};:'\",<.>/?";

    private static final SecureRandom rand = new SecureRandom();

    // REVIEW: should this include flags to not include lowercase, uppercase, digits, symbols if the user requests?
    // Generate a password with a specific length
    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);

        // Generate a password with all character types for maximum security, up to length - 4 in case the length is not divisible by 4
        while(password.length() < length - 4) {
            password.append(getRandomCharacter(LOWERCASE_LETTERS));
            password.append(getRandomCharacter(UPPERCASE_LETTERS));
            password.append(getRandomCharacter(DIGITS));
            password.append(getRandomCharacter(SYMBOLS));
        }

        // Fill in the rest of the characters after accounting for the case where length is not divisible by 4
        while(password.length() < length) {
            password.append(getRandomCharacter(LOWERCASE_LETTERS + UPPERCASE_LETTERS + DIGITS + SYMBOLS));
        }

        return shufflePassword(password.toString());
    }

    // Get a random char from a string for the password generator
    private static char getRandomCharacter(String chars) {
        return chars.charAt(rand.nextInt(chars.length()));
    }

    // Shuffles the password so that passwords are not in lowercase, uppercase, digit, symbol order everytime
    private static String shufflePassword(String password) {
        char[] passwordArray = password.toCharArray();

        // Shuffle from back to front swapping random characters in the password
        for(int i = passwordArray.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
    }
}
