//estos archivos estan de ejemplo

import java.io.*;
import java.net.*;

public class UDPCliente {

    public static void main(String args[]) throws Exception {

        BufferedReader inFromUser
                = new BufferedReader(new InputStreamReader(System.in));

        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("127.0.0.1"); //ip del sv

        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        String sentence = inFromUser.readLine(); //lectura de teclado
        sendData = sentence.getBytes(); //se almacena lectura en array de bytes
        
        //se arma el paquete con los datos y el destino especificando ip y puerto
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9875);
        clientSocket.send(sendPacket); //se envia el packete al socket declarado
        ////////////////////////////////////////////////////////////////////////////////////
        
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
        clientSocket.receive(receivePacket);

        String echoSentence = new String(receivePacket.getData());

        System.out.println("DESDE EL SERVIDOR:" + echoSentence);
        clientSocket.close();
    }
}
