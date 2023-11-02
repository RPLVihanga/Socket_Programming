import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class HeadquartersServerUDP {
    private static final int PORT = 5050;
    private static final Map<String, Double> priceMap = new HashMap<>();
    private static final Map<String, Double> discountMap = new HashMap<>();

    public static void main(String[] args) {
        // Populate the price map and discount map
        populateMaps();

        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            System.out.println("Headquarters server started and listening on port " + PORT);

            byte[] receiveBuffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                // Create a new thread to handle the client request
                ClientHandler clientHandler = new ClientHandler(serverSocket, receivePacket);
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
        private final DatagramSocket serverSocket;
        private final DatagramPacket receivePacket;

        public ClientHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
            this.serverSocket = serverSocket;
            this.receivePacket = receivePacket;
        }

        @Override
        public void run() {
            try {
                byte[] sendData;
                String itemCode = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Retrieve the initial price and discount from the maps
                Double initialPrice = priceMap.getOrDefault(itemCode, 0.0);
                Double discount = discountMap.getOrDefault(itemCode, 0.0);

                // Calculate the final price
                Double finalPrice = initialPrice * (1 - discount / 100);

                // Prepare the response data
                String response = "Item Code: " + itemCode + "\n" +
                        "Initial Price: " + initialPrice + "\n" +
                        "Discount: " + discount + "%\n" +
                        "Final Price: " + finalPrice;

                sendData = response.getBytes();

                // Send the response to the client
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                        receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}