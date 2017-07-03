

import java.net.*;

public class UDPServidor {

    public static void main(String args[]) throws Exception {

        DatagramSocket serverSocket = new DatagramSocket(9875);

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        while (true) {

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            String sentence = new String(receivePacket.getData());

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String s = sentence.toUpperCase();
            sendData = s.getBytes();
            System.out.println("cliente:" + IPAddress + "  " + s);

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            serverSocket.send(sendPacket);
        }
    }
}
