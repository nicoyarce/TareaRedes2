
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Ventana extends JFrame implements ActionListener {

    private JFrame marco;
    private JPanel panelNorte, panelCentro, panelSur;
    private JButton boton1, boton2;
    private JLabel texto;
    private Cliente cliente;
    private BufferedImage img;

    public Ventana() {
        marco = new JFrame();
        cliente = new Cliente();
        /*try {
            img = ImageIO.read(new File("C:\\Users\\Usuario\\Desktop\\Video1\\Video1 0030.jpg"));
        } catch (IOException ex) {
            System.err.println(ex);
        }*/
        panelNorte = new JPanel(new FlowLayout());
        panelSur = new JPanel(new FlowLayout());
        panelCentro = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawImage(img, 100, 100, null);                
                repaint();
                removeAll();
            }
        };
        configurarVentana();
        // creamos los componentes
        boton1 = new JButton();
        boton2 = new JButton();
        texto = new JLabel("Test");
        boton1.setText("Video 1");
        boton2.setText("Video 2");        
        marco.add(panelNorte, BorderLayout.NORTH);
        marco.add(panelCentro, BorderLayout.CENTER);
        marco.add(panelSur, BorderLayout.SOUTH);
        panelNorte.add(boton1);
        panelNorte.add(boton2);
        panelSur.add(texto);
        boton1.addActionListener(this);
        boton2.addActionListener(this);
    }

    private void configurarVentana() {
        marco.setTitle("Cliente");
        marco.setSize(800, 600);
        marco.setLocationRelativeTo(null);
        marco.setLayout(new BorderLayout());
        marco.setResizable(false);
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setVisible(true);
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
            //la funcion comprobarRespuesta crea el socket udp y se le envia el puerto al servidor
            int nFramesTotal = cliente.comprobarRespuesta(respuesta); 
            int nFramesRecibidos = 0;
            respuesta = cliente.recibirTCP();
            while (!respuesta.equals("FIN")) {
                //esta linea transforma el arreglo de bytes a una imagen                
                img = ImageIO.read(new ByteArrayInputStream(cliente.recibirVideo().getData()));
                repaint();
                removeAll();                
                nFramesRecibidos++;
                System.out.println("Imagen " + nFramesRecibidos);
                respuesta = cliente.recibirTCP();
            }
            texto.setText("FIN. Recibio " + nFramesRecibidos + " frames de un total de " + nFramesTotal);
            cliente.clientSocketTCP.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public static void main(String[] args) {
        Ventana v = new Ventana();      // creamos una ventana
    }
}
