
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class TCPServidorMultiple extends Thread {

    public Vector<BufferedReader> readers;
    public Vector<Socket> clientes;
    public Socket connectionSocket;
    public Vector<PrintWriter> writers;
    private boolean ready;
    public ArrayList<File> video1, video2;
    
    
    public TCPServidorMultiple() {
        ready = false;
        clientes = new Vector<Socket>();
        readers = new Vector<BufferedReader>();
        writers = new Vector<PrintWriter>();
        video1 = new ArrayList<File>();
        video2 = new ArrayList<File>();
        //Abrir ruta con video1
        File f = new File("D:\\Video1");
        video1.addAll(Arrays.asList(f.listFiles()));
    }

    public void add(Socket connectionSocket) throws IOException {
        ready = false;
        clientes.add(connectionSocket);
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(), true);
        readers.add(inFromClient);
        writers.add(outToClient);
        ready = true;
    }

    public void run() {

        String oracionCliente;
        while (clientes != null) {

            for (int i = 0; i < clientes.size(); ++i) {
                if (ready) // una variable para controlar concurrencia. Mejor usar locks si multiples hebras usan add(), lo que no es el caso
                {
                    BufferedReader reader = readers.get(i);
                    PrintWriter writer = writers.get(i);

                    try {
                        if (reader.ready()) // revisa si hay datos nuevos en el buffer de lectura del socket i-emo
                        {
                            oracionCliente = reader.readLine();
                            String respuesta = comprobarPeticion(oracionCliente);
                            System.out.println("Datos recibidos:" + oracionCliente + " desde " + clientes.get(i).getInetAddress().getHostAddress());
                            writer.println(respuesta);
                            
                            oracionCliente = reader.readLine();
                            System.out.println("Datos recibidos:" + oracionCliente + " desde " + clientes.get(i).getInetAddress().getHostAddress());
                            writer.flush();
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } //if
            }//for
        }//while

    }

    public static void main(String argv[]) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(7777);

        boolean run = true;
        TCPServidorMultiple server = new TCPServidorMultiple();
        server.start(); // hebra que atiende conexiones aceptadas     

        while (run) {  // hebra principal escucha intentos de conexion
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Agregando nuevo cliente");
            server.add(connectionSocket);
        }
        welcomeSocket.close();
    }

    private String comprobarPeticion(String oracionCliente) {
        if (oracionCliente.contains("GET video")){
            String nVideo = oracionCliente.substring(10);            
            switch(Integer.parseInt(nVideo)){
                case 1:
                    return "OK "+video1.size();
                case 2:
                    return "OK "+video2.size();
            }
        }
        return "";
    }
}
