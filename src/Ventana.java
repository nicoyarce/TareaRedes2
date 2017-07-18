
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Ventana implements ActionListener {

    private JFrame marco;
    private JPanel panelNorte, panelCentro, panelSur;
    private JButton boton1, boton2;
    private JLabel texto, labelImg;
    private ArrayList<ImageIcon> buffer;
    private Cliente cliente;
    private String respuesta;
    int nFramesTotal, nFramesRecibidos = 0;

    public Ventana() {
        //constructores
        marco = new JFrame("Cliente");
        cliente = new Cliente();
        panelNorte = new JPanel(new FlowLayout());
        panelSur = new JPanel(new FlowLayout());
        panelCentro = new JPanel(new FlowLayout());
        configurarVentana();
        // creamos los componentes
        boton1 = new JButton("Video 1");
        boton2 = new JButton("Video 2");
        texto = new JLabel("Presionar un boton");
        labelImg = new JLabel();
        buffer = new ArrayList();
        respuesta = "";
        //fijamos componentes
        marco.add(panelNorte, BorderLayout.NORTH);
        marco.add(panelCentro, BorderLayout.CENTER);
        marco.add(panelSur, BorderLayout.SOUTH);
        panelNorte.add(boton1);
        panelNorte.add(boton2);
        panelSur.add(texto);
        panelCentro.add(labelImg);
        boton1.addActionListener(this);
        boton2.addActionListener(this);
    }

    private void configurarVentana() {
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
                boton1.setEnabled(false);
                boton2.setEnabled(false);
            }
            if (e.getSource() == boton2) {
                cliente.enviarTCP("GET video_2");
                boton1.setEnabled(false);
                boton2.setEnabled(false);
            }
            Runnable myRun;
            myRun = new Runnable() {
                @Override
                public void run() {
                    try {
                        respuesta = cliente.recibirTCP();
                        texto.setText(respuesta);
                        //la funcion comprobarRespuesta crea el socket udp y se le envia el puerto al servidor
                        //tambien retorna el la cantidad de imagenes
                        nFramesTotal = cliente.comprobarRespuesta(respuesta);
                        nFramesRecibidos = 0;
                        respuesta = cliente.recibirTCP();
                        while (!respuesta.equals("FIN")) {
                            //se agregan las imagenes a un arrayList que hace de "buffer"
                            buffer.add(new ImageIcon((cliente.recibirVideo().getData())));
                            System.out.println("Imagen " + nFramesRecibidos);
                            //recibe respuesta del servidor para saber si termino de enviar imagenes
                            respuesta = cliente.recibirTCP();
                            nFramesRecibidos++;
                            labelImg.setIcon(buffer.get(0));
                            buffer.remove(0);
                        }
                        texto.setText("FIN. Recibio " + nFramesRecibidos + " frames de un total de " + nFramesTotal);                        
                        //cliente.clientSocketTCP.close();                        
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(marco, ex, "Error", 1);
                    }
                }
            };
            Thread t = new Thread(myRun);
            t.start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(marco, ex, "Error", 1);
        }
    }

    public static void main(String[] args) {
        Ventana v = new Ventana();      // creamos una ventana
    }
}
