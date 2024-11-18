package PasswordStorage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
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
                    //"password,txt"));
                    "/home/dannymac/Desktop/test.txt"));


            // NOTE!!! Password will be plaintext for now, will work on encryption soon.
            // DO NOT STORE ANY MEANINGFUL PASSWORD AT THE MOMENT
            String credData = ("Title: " + title + "\nUsername: " + username + "\nPassword: " + password);
            credWriter.write(credData);
            // clear memory and close writer
            credWriter.flush();
            credWriter.close();
        }
        // Catch the exception
        catch (IOException e) {

            System.out.println(e.getMessage());
        }
    }

    public static void ReadPassword() {

        try {
            // Changed the directory to desktop for easy testing, if not specified it
            // will print in project folder

            // Change the directory to make sense for you
            BufferedReader credReader = new BufferedReader(new FileReader(
                    //"password,txt"));
                    "/home/dannymac/Desktop/test.txt"));


            // NOTE!!! Password will be plaintext for now, will work on encryption soon.
            // DO NOT STORE ANY MEANINGFUL PASSWORD AT THE MOMENT
            String data;
            while ((data = credReader.readLine()) !=null) {
                System.out.println(data);
            }
        }
        // Catch the exception
        catch (IOException e) {

            System.out.println(e.getMessage());
        }

    }

    public static void AppendPassword() {

        String credFile = "/home/dannymac/Desktop/test.txt";
        String appendData = "\n\nWILL BE NEW CREDS";

        try {
            FileWriter credAppend = new FileWriter(credFile, true);
            BufferedWriter credWriter = new BufferedWriter(credAppend);

            credWriter.write(appendData);

            System.out.println("Data has been successfully appended!");
            credWriter.close();

        }

        catch (IOException e) {

            System.out.println(e.getMessage());
        }

    }
}
