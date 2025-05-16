/*
 * NAME: Main
 * AUTHOR: J. Pisani
 * DATE: 4/28/25
 *
 * DESCRIPTION: Entry point for the program
 */

package com.jgptech.Locals;

import com.jgptech.Locals.CLI.UserInput;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "locals", mixinStandardHelpOptions = true, version = "locals 0.1", description = "Locally stored and encrypted password manager")
public class Main implements Callable<Integer> {
    @Parameters(index = "0", paramLabel = "VAULT", arity = "0..1", description = "Name of the vault file to create/open")
    private String vaultName = "";

    @Option(names = {"-n", "--new"}, description = "Create a new vault")
    private boolean newVault;

    @Override
    public Integer call() throws Exception {
        if(newVault) {
            UserInput.createNewVault(vaultName);
        }

        System.out.println("Exiting Locals...");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
    }
}
