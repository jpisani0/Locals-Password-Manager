/*
 * NAME: Main
 * AUTHOR: J. Pisani
 * DATE: 4/28/25
 *
 * DESCRIPTION: Entry point for the program
 */

package com.jgptech.Locals;

import picocli.CommandLine.Option;

public class Main {
    @Option(names = {"-n", "--new"}, description = "Create a new vault")
    private boolean newVault;

    @Option(names = {"-n", "--new"}, description = "Name of vault file")
    private String vaultName = "";

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
