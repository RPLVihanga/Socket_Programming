package org.example;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client_udp {

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 55885;

            while (true) {
                // Get item code from the user
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter item code (or type 'exit' to end): ");
                String itemCode = scanner.nextLine();

                if ("exit".equalsIgnoreCase(itemCode.trim())) {
                    break; // Exit the loop if the user types 'exit'
                }

                // Convert the item code to bytes
                byte[] sendBuffer = itemCode.getBytes();

                // Create a DatagramPacket to send to the server
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);

                // Send the packet to the server
                socket.send(sendPacket);

                // Receive results from the server
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                // Convert received bytes to results
                try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()))) {
                    double initialPrice = input.readDouble();
                    double discountValue = input.readDouble();
                    double finalPrice = input.readDouble();

                    // Display results
                    System.out.println("Item Code: " + itemCode);
                    System.out.println("Initial Price: " + initialPrice);
                    System.out.println("Discount Value: " + discountValue);
                    System.out.println("Final Price: " + finalPrice);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
