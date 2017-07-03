
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Ventana implements ActionListener {

    private JFrame marco;
    private JButton boton1, boton2;
    private JLabel texto;
    private Cliente cliente;
    private BufferedImage img;

    public Ventana() {
        marco = new JFrame();
        cliente = new Cliente();
        try {
            img = ImageIO.read(new File("C:/Users/Nicoyarce/Pictures/yare yare.jpg"));
        } catch (IOException ex) {
            System.err.println(ex);
        }

        // creamos los componentes
        boton1 = new JButton();
        boton2 = new JButton();
        texto = new JLabel("Test");

        boton1.setText("Video 1");
        boton2.setText("Video 2");
        boton1.addActionListener(this);
        boton2.addActionListener(this);
        marco.add(boton1);
        marco.add(boton2);
        marco.add(texto);
        configurarVentana();
    }

    private void configurarVentana() {
        marco.setTitle("Cliente");
        marco.setSize(1366, 768);
        marco.setLocationRelativeTo(null);
        FlowLayout layout = new FlowLayout();
        marco.setLayout(layout);
        marco.setResizable(false);
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setVisible(true);
    }

    public void paint(Graphics g) {
        g.drawImage(img, 500, 500, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == boton1) {
                cliente.enviarTCP("GET video_1");
            }
            if (e.getSource() == boton2) {
                cliente.enviarTCP("GET video_2");
            }
            String respuesta = cliente.recibirTCP();
            texto.setText(respuesta);
            comprobarRespuesta(respuesta);
            recibirVideo();
            cliente.clientSocketTCP.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public static void main(String[] args) {
        Ventana v = new Ventana();      // creamos una ventana
    }

    private void comprobarRespuesta(String respuesta) throws IOException {
        int puerto = 0;
        if (respuesta.contains("OK")) {
            puerto = cliente.crearSocketUDP();
            cliente.enviarTCP("PORT " + puerto);
        }
    }

    private void recibirVideo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
