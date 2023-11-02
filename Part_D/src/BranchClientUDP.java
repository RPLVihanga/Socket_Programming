
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class BranchClientUDP {
                private static final String SERVER_IP = "localhost";
                private static final int SERVER_PORT = 9999;

                public static void main(String[] args) {
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
                        System.out.println("Connected to headquarters server...");

                        // Read the item code from the user
                        Scanner userInput = new Scanner(System.in);
                        System.out.print("Enter item code: ");
                        String itemCode = userInput.nextLine();

                        byte[] sendData = itemCode.getBytes();

                        // Send the item code to the server
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
                        socket.send(sendPacket);

                        byte[] receiveBuffer = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                        // Receive and display the response from the server
                        socket.receive(receivePacket);
                        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println(response);

                        // Close the socket and scanner
                        socket.close();
                        userInput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }