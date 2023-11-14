package org.example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.*;

public class Server_udp {

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(55885)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                socket.receive(receivePacket);
                String itemCode = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                System.out.println("Received item code from client " + clientAddress.getHostAddress() + ":" + clientPort + ": " + itemCode);

                // Fetch data from the database
                double initialPrice = fetchDataFromDatabase("SELECT price FROM prices WHERE itemcode=?", itemCode);
                double discountValue = fetchDataFromDatabase("SELECT discount FROM discount WHERE itemcode=?", itemCode);
                double finalPrice = initialPrice - discountValue;

                // Send results back to the client
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                try (ObjectOutputStream output = new ObjectOutputStream(byteStream)) {
                    output.writeDouble(initialPrice);
                    output.writeDouble(discountValue);
                    output.writeDouble(finalPrice);
                    output.flush();
                }

                byte[] sendBuffer = byteStream.toByteArray();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                socket.send(sendPacket);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static double fetchDataFromDatabase(String query, String itemCode) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medtec",
                "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, itemCode);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getDouble(1) : 0.0;
            }
        }
    }
}