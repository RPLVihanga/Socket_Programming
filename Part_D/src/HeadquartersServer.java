import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HeadquartersServer {
    private static final int PORT = 5050;
    private static final Map<String, Double> priceMap = new HashMap<>();
    private static final Map<String, Double> discountMap = new HashMap<>();

    public static void main(String[] args) {
        // Populate the price map and discount map
        populateMaps();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Headquarters server started and listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Create a new thread to handle the client request
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void populateMaps() {
        priceMap.put("MT001", 2500.00);
        priceMap.put("MT002", 1200.00);
        priceMap.put("MT003", 350.00);
        priceMap.put("MT004", 990.00);
        priceMap.put("ES001", 4000.00);
        priceMap.put("ES002", 3400.00);
        priceMap.put("ES003", 6500.00);
        priceMap.put("ES004", 1500.00);
        priceMap.put("MLS001", 750.00);
        priceMap.put("MLS002", 4500.00);

        discountMap.put("MT001", 5.0);
        discountMap.put("MT002", 2.5);
        discountMap.put("MT003", 0.0);
        discountMap.put("MT004", 2.0);
        discountMap.put("ES001", 10.0);
        discountMap.put("ES002", 7.5);
        discountMap.put("ES003", 15.0);
        discountMap.put("ES004", 5.0);
        discountMap.put("MLS001", 0.0);
        discountMap.put("MLS002", 10.0);
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Create input and output streams for client communication
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read the item code sent by the client
                String itemCode = in.readLine();

                // Retrieve the initial price and discount from the maps
                Double initialPrice = priceMap.getOrDefault(itemCode, 0.0);
                Double discount = discountMap.getOrDefault(itemCode, 0.0);

                // Calculate the final price
                Double finalPrice = initialPrice * (1 - discount / 100);

                // Send the results to the client
                out.println("Item Code: " + itemCode);
                out.println("Initial Price: " + initialPrice);
                out.println("Discount: " + discount + "%");
                out.println("Final Price: " + finalPrice);

                // Close the streams and socket
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}