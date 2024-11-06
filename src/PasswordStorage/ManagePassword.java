package PasswordStorage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;



public class ManagePassword {

    // Class will be used more later to separate writing new / managing existing passwords
    //Skip below for working code

    //private static String GetData(Scanner scanner){
        //Scanner scanner = new Scanner(System.in);

        // get credentials
        //System.out.println("Enter a Title for the credentials:");
        //System.out.println("Enter a Username");
        //String username = scanner.nextLine();
        //System.out.println("Enter a Password:");
        //String password = scanner.nextLine();

        //return scanner.nextLine();
    
    //}

    // Used to write a new Password File at the moment - Append to come soon
    // Add "ManagePassword.WritePassword();" to main to execute.
    public static void WritePassword() {

        try {
            Scanner scanner = new Scanner(System.in);

            // horrible way of getting data
            System.out.println("Enter a Title for the credentials:");
            String title = scanner.nextLine();
            System.out.println("Enter a Username");
            String username = scanner.nextLine();
            System.out.println("Enter a Password:");
            String password = scanner.nextLine();

            // Changed the directory to desktop for easy testing, if not specified it
            // will print in project folder
            // Change the directory to make sense for you
            BufferedWriter credWriter = new BufferedWriter(new FileWriter(
                    "password,txt"));
                    //"/home/dannymac/Desktop/test.txt"));


            // NOTE!!! Password will be plaintext for now, will work on ecnryption soon.
            // DO NOT STORE ANY MEANINGFUL PASSWORD AT THE MOMENT
            String testData = ("Title: " + title + "\nUsername: " + username + "\nPassword: " + password);
            credWriter.write(testData);
            // clear memory and close writer
            credWriter.flush();
            credWriter.close();
        }
        // Catch the exception
        catch (IOException e) {

            System.out.println(e.getMessage());
        }
    }
}
