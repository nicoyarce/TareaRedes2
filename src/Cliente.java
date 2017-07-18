
import java.io.*;
import java.net.*;
import java.util.Random;

public class Cliente {

    String oracionTeclado, echoSentence = "";
    BufferedReader entradaDelUsuario;
    Socket clientSocketTCP;
    DatagramSocket clientSocketUDP;
    DatagramPacket paquete;
    PrintWriter alServidor;
    BufferedReader entradaDelServidor;

    public Cliente() {
        //fijar entrada por teclado
        entradaDelUsuario = new BufferedReader(new InputStreamReader(System.in));
        try {
            clientSocketTCP = new Socket("127.0.0.1", 7777);
            //enviar datos al servidor
            alServidor = new PrintWriter(clientSocketTCP.getOutputStream(), true);
            //recibir datos desde el servidor
            entradaDelServidor = new BufferedReader(new InputStreamReader(clientSocketTCP.getInputStream()));
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void enviarTCP(String texto) throws IOException {
        //oracionTeclado = entradaDelUsuario.readLine();
        alServidor.println(texto);
    }

    public String recibirTCP() throws IOException {
        echoSentence = entradaDelServidor.readLine();
        return echoSentence;
    }

    public int crearSocketUDP() throws SocketException {
        Random r = new Random();
        int port = r.nextInt((25000 - 1025) + 1) + 1025;
        clientSocketUDP = new DatagramSocket(port);
        return port;
    }

    /*la funcion comprobarRespuesta crea el socket udp y se le envia el puerto al servidor*/
    public int comprobarRespuesta(String respuesta) throws IOException {
        int puerto = 0;
        if (respuesta.contains("OK")) {
            puerto = crearSocketUDP();
            enviarTCP("PORT " + puerto);
            int nFrames = Integer.parseInt(respuesta.substring(3));
            return nFrames;
        }
        return 0;
    }

    /*esta funcion retorna el paquete a la ventana para que pueda ser mostrado*/
    public DatagramPacket recibirVideo() throws IOException {
        byte[] receiveData = new byte[500000];
        paquete = new DatagramPacket(receiveData, receiveData.length);
        clientSocketUDP.receive(paquete);
        return paquete;
    }
}
