
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class ServidorHebras extends Thread {

    /*EDITAR ESTO!!!
    Poner carpetas con imagenes*////////////
    //Revisar main para ver si se agregan los archivos al arrayList
    public static final String RUTA1 = "D:\\Video1\\";
    public static final String RUTA2 = "D:\\Video2\\";
    public static ArrayList<File> video1, video2;
    public BufferedReader reader;
    public PrintWriter writer;
    public Socket connectionSocket;
    public DatagramSocket serverSocket;

    public ServidorHebras(Socket s) {
        connectionSocket = s;
        try {
            reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            writer = new PrintWriter(connectionSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Problema creacion socket");
        }
    }

    public void run() {
        //while(true){
        try {
            byte[] img;
            String oracionCliente;
            oracionCliente = reader.readLine(); //recibe el GET video
            int video = comprobarPeticion(oracionCliente, writer);  //comprueba el video para enviar el tamaño y retorna el numero video elegido
            System.out.println("Datos recibidos:" + oracionCliente + " desde " + connectionSocket.getInetAddress().getHostAddress());
            oracionCliente = reader.readLine(); //recibe el PORT mas el numero de puerto
            System.out.println("Datos recibidos:" + oracionCliente + " desde " + connectionSocket.getInetAddress().getHostAddress());
            String ipCliente = connectionSocket.getInetAddress().getHostAddress(); //guarda la ip del cliente en un string
            int puertoUDPCliente = Integer.parseInt(oracionCliente.substring(5)); //guarda el puerto UDP para transmitir
            InetAddress IPCliente = InetAddress.getByName(ipCliente); //declara la ip en un objeto InetAdress
            serverSocket = new DatagramSocket(); //crea socket UDP
            switch (video) {    //revisa opcion de video
                case 1:
                    for (int j = 0; j < video1.size(); j++) { //itera por el arraylist de video 1
                        img = transformarImagen(video1.get(j));
                        DatagramPacket packet = new DatagramPacket(img, img.length, IPCliente, puertoUDPCliente);
                        writer.println("NOFIN"); //se envia este mensaje para indicar al cliente que quedan imagenes
                        serverSocket.send(packet);
                        Thread.sleep(100);
                    }
                case 2:
                    for (int j = 0; j < video2.size(); j++) { //itera por el arraylist de video 2
                        img = transformarImagen(video2.get(j));
                        DatagramPacket packet = new DatagramPacket(img, img.length, IPCliente, puertoUDPCliente);
                        writer.println("NOFIN"); //se envia este mensaje para indicar al cliente que quedan imagenes
                        serverSocket.send(packet);
                        Thread.sleep(100);
                    }
            }
            writer.println("FIN"); //se envia este mensaje para indicar al cliente que termino de enviar imagenes
            writer.flush();
        } catch (IOException e) {
            System.err.println("Problema lectura/escritura en socket");
        } catch (InterruptedException ex) {
            Logger.getLogger(ServidorHebras.class.getName()).log(Level.SEVERE, null, ex);
        }
        // }
        // }
    }

    public static void main(String argv[]) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(7777);

        video1 = new ArrayList<>();
        video2 = new ArrayList<>();
        File directorio1 = new File(RUTA1);
        video1.addAll(Arrays.asList(directorio1.listFiles()));
        File directorio2 = new File(RUTA2);
        video2.addAll(Arrays.asList(directorio2.listFiles()));
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            // crear hebra para nuevo cliente
            ServidorHebras clienteNuevo
                    = new ServidorHebras(connectionSocket);
            clienteNuevo.start();
        }
        // si se usa una condicion para quebrar el ciclo while, se deben cerrar los sockets!
        // connectionSocket.close(); 
        // welcomeSocket.close(); 
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
                    writer.println("OK " + video2.size());
                    return 2;
            }
            writer.flush();
        }
        return 0;
    }

    /*esta funcion transforma el archivo jpg a un arreglo de bytes*/
    private byte[] transformarImagen(File foto) throws IOException {
        BufferedImage img = ImageIO.read(foto);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        baos.flush();
        byte[] buffer = baos.toByteArray();
        return buffer;
    }
}
