
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    String oracionTeclado, echoSentence = "";
    BufferedReader entradaDelUsuario;
    Socket clientSocketTCP;
    DatagramSocket clientSocketUDP;
    PrintWriter alServidor;
    BufferedReader entradaDelServidor;
    InetAddress IPAddress;
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];

    public Cliente() {
        //fijar entrada por teclado
        entradaDelUsuario = new BufferedReader(new InputStreamReader(System.in));
        try {
            //enviar datos al servidor
            clientSocketTCP = new Socket("127.0.0.1", 7777);
            //enviar datos al servidor
            alServidor = new PrintWriter(clientSocketTCP.getOutputStream(), true);
            //recibir datos desde el servidor
            entradaDelServidor = new BufferedReader(new InputStreamReader(clientSocketTCP.getInputStream()));
        } catch (IOException ex) {
            System.err.println(ex);
        }
        try {
            clientSocketUDP = new DatagramSocket();
        } catch (SocketException ex) {
            System.err.println(ex);
        }
        try {
            IPAddress = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException ex) {
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
    
}
