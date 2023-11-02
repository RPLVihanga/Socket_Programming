import java.io.*;
import java.net.Socket;

public class BranchClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5050;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to headquarters server...");

            // Create input and output streams for server communication
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Read the item code from the user
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter item code: ");
            String itemCode = userInput.readLine();

            // Send the item code to the server
            out.println(itemCode);

            // Receive and display the results from the server
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            // Close the streams and socket
            in.close();
            out.close();
            userInput.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}