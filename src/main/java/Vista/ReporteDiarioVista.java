package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * Ventana que muestra el texto del reporte diario. Antes se llamaba ReporteDiario, igual que la
 * clase de modelo de LogicaNegocio.Reportes, y los dos nombres chocaban.
 *
 * @author jonap
 */
public class ReporteDiarioVista extends JFrame {

    private final JTextArea areaReporte = new JTextArea(24, 60);

    public ReporteDiarioVista(String reporte, Component ventanaPadre) {
        setTitle("Reporte diario de compras");
        // cerrar el reporte no debe cerrar toda la aplicación
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        areaReporte.setText(reporte);
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaReporte.setMargin(new java.awt.Insets(8, 8, 8, 8));
        areaReporte.setCaretPosition(0);

        JButton botonCerrar = new JButton("CERRAR");
        botonCerrar.addActionListener(e -> dispose());
        JPanel abajo = new JPanel();
        abajo.setOpaque(false);
        abajo.add(botonCerrar);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(153, 153, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(new JLabel("REPORTE DIARIO COMPRAS", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(areaReporte), BorderLayout.CENTER);
        panel.add(abajo, BorderLayout.SOUTH);

        setContentPane(panel);
        pack();
        setLocationRelativeTo(ventanaPadre);
    }
}
