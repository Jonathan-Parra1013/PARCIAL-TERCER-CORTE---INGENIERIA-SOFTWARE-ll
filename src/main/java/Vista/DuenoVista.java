/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import LogicaNegocio.Moneda;
import LogicaNegocio.Producto.Producto;
import LogicaNegocio.Producto.ProductoException;
import LogicaNegocio.Producto.ProductoService;
import LogicaNegocio.Reportes.ReporteService;
import LogicaNegocio.Usuario;
import LogicaNegocio.UsuarioService;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jonap
 */
public class DuenoVista extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DuenoVista.class.getName());

    private final Usuario vendedor;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final ReporteService reporteService;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[][]{}, new String[]{"Producto", "Stock", "Precio"}) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private List<Producto> productos = new ArrayList<>();

  
    public DuenoVista(Usuario vendedor, UsuarioService usuarioService,
                      ProductoService productoService, ReporteService reporteService) {
        this.vendedor = vendedor;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.reporteService = reporteService;
        initComponents();
        configurar();
        setLocationRelativeTo(null);
        cargarProductos();
    }

    private void configurar() {
        setTitle("Vendedor - " + vendedor.getNombre());
        jTable1.setModel(modelo);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().setReorderingAllowed(false);
        // No repitas addActionListener aquí porque initComponents() ya los enlazó
    }

    private void cargarProductos() {
        try {
            productos = productoService.productosDe(vendedor);
        } catch (ProductoException ex) {
            mostrarAviso(ex.getMessage());
            productos = new ArrayList<>();
        }
        modelo.setRowCount(0);
        for (Producto p : productos) {
            modelo.addRow(new Object[]{p.getNombre(), p.getStock(), Moneda.pesos(p.getPrecio())});
        }
    }

    private Producto productoSeleccionado() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.",
                    "Tienda online", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return productos.get(fila);
    }

    private void mostrarAviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Tienda online", JOptionPane.WARNING_MESSAGE);
    }


    private void guardarProducto(Producto base) {
        FormularioProducto form = new FormularioProducto(productoService.categorias(), base);
        while (true) {
            int r = JOptionPane.showConfirmDialog(this, form,
                    base == null ? "Agregar producto" : "Modificar producto",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (r != JOptionPane.OK_OPTION) {
                return;
            }
            try {
                BigDecimal precio = form.precio();
                int stock = form.stock();
                if (base == null) {
                    productoService.publicar(vendedor, form.nombre(), form.descripcion(),
                            form.categoria(), precio, stock, form.imagen());
                } else {
                    productoService.modificar(vendedor, base, form.nombre(), form.descripcion(),
                            form.categoria(), precio, stock, form.imagen());
                }
                cargarProductos();
                return;
            } catch (ProductoException ex) {
                // se vuelve a abrir el formulario con lo que el usuario ya había escrito
                mostrarAviso(ex.getMessage());
            }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        BotonAgregar = new javax.swing.JButton();
        BotonModificar = new javax.swing.JButton();
        BotonEliminar = new javax.swing.JButton();
        BotonSalir = new javax.swing.JButton();
        BotonGenerar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));

        jLabel1.setText("VENDEDOR");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Producto", "Stock", "Precio"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("Productos");

        BotonAgregar.setText("Agregar");
        BotonAgregar.addActionListener(this::BotonAgregarActionPerformed);

        BotonModificar.setText("Modificar");
        BotonModificar.addActionListener(this::BotonModificarActionPerformed);

        BotonEliminar.setText("Eliminar");
        BotonEliminar.addActionListener(this::BotonEliminarActionPerformed);

        BotonSalir.setText("SALIR");
        BotonSalir.addActionListener(this::BotonSalirActionPerformed);

        BotonGenerar.setText("GENERAR");
        BotonGenerar.addActionListener(this::BotonGenerarActionPerformed);

        jLabel3.setText("GENERAR REPORTE DIARIO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(BotonSalir)
                        .addGap(76, 76, 76)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(BotonAgregar)
                .addGap(69, 69, 69)
                .addComponent(BotonModificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(BotonEliminar)
                .addGap(26, 26, 26))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(BotonGenerar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(BotonSalir))
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BotonAgregar)
                    .addComponent(BotonModificar)
                    .addComponent(BotonEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotonGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAgregarActionPerformed
        guardarProducto(null);
    }//GEN-LAST:event_BotonAgregarActionPerformed

    private void BotonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonSalirActionPerformed
        dispose();
    }//GEN-LAST:event_BotonSalirActionPerformed

    private void BotonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonModificarActionPerformed
        Producto seleccionado = productoSeleccionado();
        if (seleccionado != null) {
            guardarProducto(seleccionado);
        }
    }//GEN-LAST:event_BotonModificarActionPerformed

    private void BotonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonEliminarActionPerformed
        Producto seleccionado = productoSeleccionado();
        if (seleccionado == null) {
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar el producto \"" + seleccionado.getNombre() + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                productoService.eliminar(vendedor, seleccionado);
                cargarProductos();
            } catch (ProductoException ex) {
                mostrarAviso(ex.getMessage());
            }
        }
    }//GEN-LAST:event_BotonEliminarActionPerformed

    private void BotonGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonGenerarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BotonGenerarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new DuenoVista().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonAgregar;
    private javax.swing.JButton BotonEliminar;
    private javax.swing.JButton BotonGenerar;
    private javax.swing.JButton BotonModificar;
    private javax.swing.JButton BotonSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
