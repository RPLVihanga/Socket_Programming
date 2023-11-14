/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab08_dcc_medtech;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class ClientSide {
    public static void main(String[] args) {
        try {
            while (true) {
                Socket socket = new Socket("localhost", 55885); // Connect to the server on localhost and port 5555

                // Create streams for communication
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                // Get item code from the user
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter item code (or type 'exit' to end): ");
                String itemCode = scanner.nextLine();

                if ("exit".equalsIgnoreCase(itemCode.trim())) {
                    break; // Exit the loop if the user types 'exit'
                }

                // Send item code to the server
                output.writeObject(itemCode);
                output.flush();

                // Receive results from the server
                double initialPrice = input.readDouble();
                double discountValue = input.readDouble();
                double finalPrice = input.readDouble();

                // Display results
                System.out.println("Item Code: " + itemCode);
                System.out.println("Initial Price: " + initialPrice);
                System.out.println("Discount Value: " + discountValue);
                System.out.println("Final Price: " + finalPrice);

                // Close the connection
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
