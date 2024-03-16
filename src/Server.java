/**
 * SYSC 3303 Assignment 4
 * Joseph Vretenar - 101234613
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * The type Server.
 */
public class Server {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress host = InetAddress.getLocalHost();
            int port = 24;
            while (true) {
                byte[] requestData = {1};
                DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, host, port);
                socket.send(requestPacket);
                System.out.println("Sent data request to Intermediate Host");

                byte[] data = new byte[20];
                DatagramPacket dataPacket = new DatagramPacket(data, data.length);
                socket.receive(dataPacket);
                System.out.println("Received data from intermediate" + DataProcessor.processRequest(data));

                byte[] acknowledged = buildResponse(data[1]);
                DatagramPacket acknowledgedPacket = new DatagramPacket(acknowledged, acknowledged.length, host, port);
                socket.send(acknowledgedPacket);
                System.out.println("Sent acknowledgment to Intermediate Host: " + DataProcessor.processResponse(acknowledged));

                byte[] response = new byte[20];
                DatagramPacket responsePacket = new DatagramPacket(response, response.length);
                socket.receive(responsePacket);
                System.out.println("Response from Intermediate Host: " + DataProcessor.interResponse(response) + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] buildResponse(int opcode) {
        byte[] response = new byte[4];
        response[0] = 0;
        if (opcode == 1) {
            response[1] = 3;
        } else {
            response[1] = 4;
        }
        response[2] = 0;
        response[3] = 1;
        return response;
    }
}

