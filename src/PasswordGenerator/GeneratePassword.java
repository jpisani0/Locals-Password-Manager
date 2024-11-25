package PasswordGenerator;

import java.util.Random;
import java.util.Scanner;

public class GeneratePassword {

    public GeneratePassword(){

        Scanner scanner = new Scanner(System.in);
        int length;


        boolean dontRun = false;
        while (!dontRun) {

            System.out.println("Do you want to generate a new password?");
            System.out.println("Yes || No?");
            String execute = scanner.nextLine();


            if (execute.equalsIgnoreCase("y")) {
                boolean validNum = false;
                while (!validNum) {

                    System.out.println("Please enter a password length:");
                    scanner.hasNextInt();

                    if (true) {
                        length = scanner.nextInt();
                        System.out.println("Password has a length of " + length);
                        validNum = true;
                        dontRun = true;
                    } else {
                        System.out.println("Please enter a valid number!");
                    }
                }

            } else if (execute.equalsIgnoreCase("n")) {
                System.out.println("Okay!");
                dontRun = true;
            } else {
                System.out.println("INVALID INPUT!!");
                System.out.println("Please enter 'y' or 'n'");

            }

        }






    }
}
