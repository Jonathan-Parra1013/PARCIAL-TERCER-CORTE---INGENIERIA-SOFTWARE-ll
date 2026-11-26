/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

/**
 *
 * @author jonap
 */
public class ReporteDiarioVista extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ReporteDiarioVista.class.getName());

    // datos estructurados del reporte (los que usan los exportadores); null si solo se muestra el texto
    private final LogicaNegocio.Reportes.ReporteDiario datos;

    /**
     * Versión sin datos estructurados: solo muestra el texto (los botones de exportar quedan desactivados).
     *
     * @param reporte      el texto del reporte que se muestra
     * @param ventanaPadre ventana desde la que se abre (para centrar esta encima); puede ser null
     */
    public ReporteDiarioVista(String reporte, java.awt.Component ventanaPadre) {
        this(reporte, null, ventanaPadre);
    }

    /**
     * @param reporte      el texto del reporte que se muestra
     * @param datos        el mismo reporte como objeto, para exportarlo a CSV o PDF; puede ser null
     * @param ventanaPadre ventana desde la que se abre (para centrar esta encima); puede ser null
     */
    public ReporteDiarioVista(String reporte, LogicaNegocio.Reportes.ReporteDiario datos,
                              java.awt.Component ventanaPadre) {
        this.datos = datos;
        initComponents();
        configurar(reporte);
        setLocationRelativeTo(ventanaPadre);
    }

    // Todo lo que se agrega a mano va fuera de initComponents() para que el
    // editor de NetBeans no lo borre al regenerar el formulario.
    private void configurar(String reporte) {
        setTitle("Reporte diario de compras");
        // el formulario trae EXIT_ON_CLOSE: cerrar el reporte cerraría toda la aplicación
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // el texto va dentro del panel con scroll (RegistroDiario) que ya está en tu diseño
        javax.swing.JTextArea areaReporte = new javax.swing.JTextArea(reporte);
        areaReporte.setEditable(false);
        areaReporte.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 12));
        areaReporte.setMargin(new java.awt.Insets(8, 8, 8, 8));
        areaReporte.setCaretPosition(0);
        RegistroDiario.setViewportView(areaReporte);

        BotonCerrar.addActionListener(e -> dispose());

        agregarBarraExportar();
    }

    // Barra con los botones de exportación, agregada por código (fuera de initComponents)
    // para que el editor de NetBeans no la borre al regenerar el formulario.
    private void agregarBarraExportar() {
        javax.swing.JButton botonCsv = new javax.swing.JButton("EXPORTAR CSV");
        javax.swing.JButton botonPdf = new javax.swing.JButton("EXPORTAR PDF");
        // Strategy: la ventana elige el exportador; el formato lo resuelve cada clase
        botonCsv.addActionListener(e -> exportar(new LogicaNegocio.Reportes.ExportadorCSV()));
        botonPdf.addActionListener(e -> exportar(new LogicaNegocio.Reportes.ExportadorPDF()));
        // sin los datos estructurados no hay nada que exportar
        botonCsv.setEnabled(datos != null);
        botonPdf.setEnabled(datos != null);

        javax.swing.JPanel barra = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 12, 8));
        barra.setBackground(new java.awt.Color(153, 153, 255)); // el mismo morado del formulario
        barra.add(botonCsv);
        barra.add(botonPdf);

        // el formulario queda en el centro y la barra debajo
        java.awt.Container formulario = getContentPane();
        javax.swing.JPanel raiz = new javax.swing.JPanel(new java.awt.BorderLayout());
        setContentPane(raiz);
        raiz.add(formulario, java.awt.BorderLayout.CENTER);
        raiz.add(barra, java.awt.BorderLayout.SOUTH);
        pack();
    }

    // Pide dónde guardar el archivo y delega la escritura en el exportador elegido
    private void exportar(LogicaNegocio.Reportes.ExportadorReporte exportador) {
        String extension = exportador.extension();

        javax.swing.JFileChooser selector = new javax.swing.JFileChooser();
        selector.setDialogTitle("Guardar reporte (" + extension.toUpperCase() + ")");
        selector.setSelectedFile(new java.io.File("reporte_ventas_" + datos.getFecha() + "." + extension));
        selector.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                extension.toUpperCase(), extension));
        if (selector.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.nio.file.Path destino = selector.getSelectedFile().toPath();
        if (!destino.getFileName().toString().toLowerCase().endsWith("." + extension)) {
            destino = destino.resolveSibling(destino.getFileName() + "." + extension);
        }
        if (java.nio.file.Files.exists(destino)) {
            int r = javax.swing.JOptionPane.showConfirmDialog(this,
                    "El archivo ya existe. ¿Quieres reemplazarlo?", "Tienda online",
                    javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
            if (r != javax.swing.JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            exportador.exportar(datos, destino);
            javax.swing.JOptionPane.showMessageDialog(this, "Reporte guardado en:\n" + destino,
                    "Tienda online", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (java.io.IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No se pudo guardar el archivo.\n" + ex.getMessage(),
                    "Tienda online", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        RegistroDiario = new javax.swing.JScrollPane();
        BotonCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jLabel1.setText("REPORTE DIARIO COMPRAS");

        BotonCerrar.setText("CERRAR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(118, 118, 118))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(33, 33, 33)
                                                .addComponent(RegistroDiario, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(148, 148, 148)
                                                .addComponent(BotonCerrar)))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel1)
                                .addGap(28, 28, 28)
                                .addComponent(RegistroDiario, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(BotonCerrar)
                                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonCerrar;
    private javax.swing.JScrollPane RegistroDiario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
