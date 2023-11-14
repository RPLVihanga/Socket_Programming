
package lab08_dcc_medtech;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ClientHandler extends Thread{

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(55885)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            String itemCode = (String) input.readObject();
            System.out.println("Received item code from client: " + itemCode);

            // Fetch data from the database
            double initialPrice = fetchDataFromDatabase("SELECT price FROM prices WHERE itemcode=?", itemCode);
            double discountValue = fetchDataFromDatabase("SELECT discount FROM discount WHERE itemcode=?", itemCode);
            double finalPrice = initialPrice - discountValue;

            // Send results back to the client
            output.writeDouble(initialPrice);
            output.writeDouble(discountValue);
            output.writeDouble(finalPrice);
            output.flush();

        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
