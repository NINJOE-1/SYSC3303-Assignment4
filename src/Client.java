/**
 * SYSC 3303 Assignment 4
 * Joseph Vretenar - 101234613
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * The type Client.
 */
public class Client {
    static byte[] zeroByte = {0};
    static byte[] oneByte = {1};
    static byte[] twoByte = {2};
    static byte[] file = "test.txt".getBytes(StandardCharsets.UTF_8);
    static byte[] mode = "netascii".getBytes(StandardCharsets.UTF_8);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress host = InetAddress.getLocalHost();
            int port = 23;
            byte[] data;
            for (int i = 1; i <= 11; i++) {
                if (i % 2 == 0) {
                    data = createRequest(2);
                } else {
                    data = createRequest(1);
                }
                DatagramPacket packet = new DatagramPacket(data, data.length, host, port);
                socket.send(packet);
                String strRequest = DataProcessor.processRequest(data);
                System.out.println("Sent request " + strRequest);

                byte[] response = new byte[20];
                DatagramPacket responsePacket = new DatagramPacket(response, response.length);
                socket.receive(responsePacket);
                System.out.println("Response from Intermediate Host: " + DataProcessor.interResponse(response));

                byte[] request = {1};
                DatagramPacket requestPacket = new DatagramPacket(request, request.length, host, port);
                socket.send(requestPacket);
                System.out.println("Sent acknowledgment request to Intermediate Host");

                byte[] acknowledged = new byte[4];
                DatagramPacket acknowledgedPacket = new DatagramPacket(acknowledged, acknowledged.length);
                socket.receive(acknowledgedPacket);
                String resData = DataProcessor.processResponse(acknowledged);
                System.out.println("Received acknowledgement from Intermediate Host " + resData + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] createRequest(int opcode) throws IOException {
        byte[] output = new byte[file.length + mode.length + 4];
        ByteBuffer buffer = ByteBuffer.wrap(output);
        buffer.put(zeroByte);
        if (opcode == 1)
            buffer.put(oneByte);
        else if (opcode == 2)
            buffer.put(twoByte);
        else
            throw new IOException("Invalid opcode");
        buffer.put(zeroByte);
        buffer.put(file);
        buffer.put(zeroByte);
        buffer.put(mode);
        output = buffer.array();
        return output;
    }
}
