
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.imageio.ImageIO;   

public class ServidorMultiple extends Thread{
    /*EDITAR ESTO!!!
    Poner carpetas con imagenes*////////////
    String RUTA1 = "D:\\Video1\\";
    String RUTA2 = "D:\\Video2\\";
    
    public Vector<BufferedReader> readers;
    public Vector<Socket> clientes;    
    public Vector<PrintWriter> writers;
    public Socket connectionSocket;
    private boolean ready;

    public DatagramSocket serverSocket;
    public ArrayList<File> video1, video2;

    public ServidorMultiple() {
        ready = false;
        clientes = new Vector<Socket>();
        readers = new Vector<BufferedReader>();
        writers = new Vector<PrintWriter>();
        video1 = new ArrayList<File>();
        video2 = new ArrayList<File>();
        
        File f = new File(RUTA1);
        System.out.println("Video 1 tiene " + (f.listFiles().length) + " fotos");
        video1.addAll(Arrays.asList(f.listFiles()));
        f = new File(RUTA2);
        System.out.println("Video 2 tiene " + (f.listFiles().length) + " fotos");
        video2.addAll(Arrays.asList(f.listFiles()));
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
        while (clientes != null) {
            for (int i = 0; i < clientes.size(); ++i) {
                if (ready) // una variable para controlar concurrencia. Mejor usar locks si multiples hebras usan add(), lo que no es el caso
                {
                    BufferedReader reader = readers.get(i);
                    PrintWriter writer = writers.get(i);
                    byte[] img;
                    String oracionCliente;
                    try {
                        if (reader.ready()) // revisa si hay datos nuevos en el buffer de lectura del socket i-emo
                        {                            
                            oracionCliente = reader.readLine(); //recibe el GET video
                            int video = comprobarPeticion(oracionCliente, writer);  //retorna el numero video elegido
                            System.out.println("Datos recibidos:" + oracionCliente + " desde " + clientes.get(i).getInetAddress().getHostAddress());
                            oracionCliente = reader.readLine(); //recibe el PORT mas el numero
                            System.out.println("Datos recibidos:" + oracionCliente + " desde " + clientes.get(i).getInetAddress().getHostAddress());

                            String ipCliente = clientes.get(i).getInetAddress().getHostAddress(); //guarda la ip del cliente en un string
                            int puertoUDPCliente = Integer.parseInt(oracionCliente.substring(5)); //guarda el puerto UDP para transmitir                              
                            InetAddress IPCliente = InetAddress.getByName(ipCliente); //declara la ip en un objeto InetAdress
                            serverSocket = new DatagramSocket(); //crea socket UDP
                            switch (video) {    //revisa opcion de video
                                case 1:
                                    for (int j = 0; j < video1.size(); j++) {
                                        img = transformarImagen(video1.get(j));
                                        DatagramPacket packet = new DatagramPacket(img, img.length, IPCliente, puertoUDPCliente);
                                        writer.println("NOFIN"); //se envia este mensaje para indicar al cliente que quedan imagenes
                                        serverSocket.send(packet);                                        
                                        Thread.sleep(100);                                        
                                    }
                                case 2:
                                    for (int j = 0; j < video2.size(); j++) {
                                        img = transformarImagen(video2.get(j));
                                        DatagramPacket packet = new DatagramPacket(img, img.length, IPCliente, puertoUDPCliente);
                                        writer.println("NOFIN"); //se envia este mensaje para indicar al cliente que quedan imagenes
                                        serverSocket.send(packet);
                                        Thread.sleep(250);
                                    }
                            }
                            writer.println("FIN");
                            writer.flush();
                        }
                    } catch (IOException | InterruptedException ex) {
                        System.err.println(ex);
                    }
                } //if
            }//for
        }//while

    }

    public static void main(String argv[]) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(7777);
        boolean run = true;
        ServidorMultiple server = new ServidorMultiple();
        server.start(); // hebra que atiende conexiones aceptadas     

        while (run) {  // hebra principal escucha intentos de conexion
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Agregando nuevo cliente");
            server.add(connectionSocket);
        }
        welcomeSocket.close();
    }

    /*esta funcion comprueba el GET video y envia el OK con el tamaño*/
    private int comprobarPeticion(String oracionCliente, PrintWriter writer) {
        if (oracionCliente.contains("GET video")) {
            String nVideo = oracionCliente.substring(10); //corta el final del string
            switch (Integer.parseInt(nVideo)) {
                case 1:
                    writer.println("OK " + video1.size());
                    return 1;
                case 2:
                    writer.println("OK " + video1.size());
                    return 2;
            }
            writer.flush();
        }
        return 0;
    }

    private byte[] transformarImagen(File foto) throws IOException {
        BufferedImage img = ImageIO.read(foto);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        baos.flush();
        byte[] buffer = baos.toByteArray();
        return buffer;
    }
}
