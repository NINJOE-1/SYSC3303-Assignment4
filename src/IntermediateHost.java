/**
 * SYSC 3303 Assignment 4
 * Joseph Vretenar - 101234613
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

/**
 * The type Intermediate host.
 */
public class IntermediateHost {
    /**
     * The Client response.
     */
    static byte[] clientResponse = "forwarding to server".getBytes(StandardCharsets.UTF_8);
    /**
     * The Server response.
     */
    static byte[] serverResponse = "forwarding to client".getBytes(StandardCharsets.UTF_8);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket(23);
            DatagramSocket serverSocket = new DatagramSocket(24);
            while (true) {
                byte[] data = new byte[20];
                DatagramPacket clientPacket = new DatagramPacket(data, data.length);
                clientSocket.receive(clientPacket);
                String strData = DataProcessor.processRequest(data);
                System.out.println("Received request from client" + strData);
                DatagramPacket responsePacket = new DatagramPacket(clientResponse, clientResponse.length, clientPacket.getAddress(), clientPacket.getPort());
                clientSocket.send(responsePacket);
                System.out.println("Confirmed data received to client\n");

                byte[] request = new byte[0];
                DatagramPacket requestPacket = new DatagramPacket(request, request.length);
                serverSocket.receive(requestPacket);
                System.out.println("Received data request from server");
                DatagramPacket forwardPacket = new DatagramPacket(data, data.length, requestPacket.getAddress(), requestPacket.getPort());
                serverSocket.send(forwardPacket);
                System.out.println("Sent data to server\n");

                byte[] acknowledged = new byte[4];
                DatagramPacket acknowledgedPacket = new DatagramPacket(acknowledged, acknowledged.length);
                serverSocket.receive(acknowledgedPacket);
                System.out.println("Received acknowledged data from server: " + DataProcessor.processResponse(acknowledged));
                DatagramPacket responsePacket2 = new DatagramPacket(serverResponse, serverResponse.length, acknowledgedPacket.getAddress(), acknowledgedPacket.getPort());
                serverSocket.send(responsePacket2);
                System.out.println("Confirmed acknowledgment received to server\n");

                byte[] request2 = new byte[0];
                DatagramPacket requestPacket2 = new DatagramPacket(request2, request2.length);
                clientSocket.receive(requestPacket2);
                System.out.println("Received acknowledgment request from client");
                DatagramPacket forwardPacket2 = new DatagramPacket(acknowledged, acknowledged.length, requestPacket2.getAddress(), requestPacket2.getPort());
                clientSocket.send(forwardPacket2);
                System.out.println("Sent acknowledgment to client\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
