/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import LogicaNegocio.Carrito.CarritoException;
import LogicaNegocio.Carrito.CarritoService;
import LogicaNegocio.Carrito.ItemCarrito;
import LogicaNegocio.Moneda;
import LogicaNegocio.Pedido.PedidoService;
import LogicaNegocio.Usuario;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jonap
 */
public class CarritoVista extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CarritoVista.class.getName());

    private final CarritoService carrito;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[][]{}, new String[]{"Producto", "Cantidad", "Precio", "Subtotal"}) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private String ultimaInstantanea = "";
    private final Usuario cliente;
    private final PedidoService pedidos;
    private final Runnable alComprar; // se ejecuta cuando la compra se confirma (refresca el catálogo)

  
    public CarritoVista(java.awt.Component padre, CarritoService carrito,
                        Usuario cliente, PedidoService pedidos, Runnable alComprar) {
        this.carrito = carrito;
        this.cliente = cliente;
        this.pedidos = pedidos;
        this.alComprar = alComprar;
        initComponents();
        configurar();
        setLocationRelativeTo(padre);
        refrescar();
    }

    // Todo lo que se agrega a mano va fuera de initComponents() para que el
    // editor de NetBeans no lo borre al regenerar el formulario.
    private void configurar() {
        // el formulario trae EXIT_ON_CLOSE: cerrar el carrito cerraría toda la aplicación
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        jTable1.setModel(modelo);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarSeleccion();
            }
        });

        // PRODUCTO y PRECIO se llenan al elegir una fila; solo CANTIDAD se escribe
        EspacioProducto.setEditable(false);
        EspacioPrecio.setEditable(false);

        // Esta ventana no es modal: al volver a ella se actualiza si el cliente agregó productos mientras tanto
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
                if (!instantanea().equals(ultimaInstantanea)) {
                    refrescar();
                }
            }
        });
    }

    /** Resumen del contenido del carrito, para saber si cambió desde la última vez que se dibujó. */
    private String instantanea() {
        StringBuilder sb = new StringBuilder();
        for (ItemCarrito item : carrito.getItems()) {
            sb.append(item.getProducto().getId()).append(':').append(item.getCantidad()).append(';');
        }
        return sb.toString();
    }

    private void refrescar() {
        int fila = jTable1.getSelectedRow();
        modelo.setRowCount(0);
        for (ItemCarrito item : carrito.getItems()) {
            modelo.addRow(new Object[]{
                item.getProducto().getNombre(),
                item.getCantidad(),
                Moneda.pesos(item.getPrecioUnitario()),
                Moneda.pesos(item.getSubtotal())});
        }
        ultimaInstantanea = instantanea();
        if (fila >= 0 && fila < modelo.getRowCount()) {
            jTable1.setRowSelectionInterval(fila, fila); // conserva la fila elegida
        } else {
            mostrarSeleccion(); // sin fila elegida: limpia los campos
        }
        // el diseño no tiene una etiqueta de subtotal: se muestra en el título de la ventana
        setTitle(carrito.estaVacio()
                ? "Carrito (vacío)"
                : "Carrito - Subtotal: " + Moneda.pesos(carrito.getSubtotal()));
    }

    private void mostrarSeleccion() {
        int fila = jTable1.getSelectedRow();
        List<ItemCarrito> items = carrito.getItems();
        if (fila < 0 || fila >= items.size()) {
            EspacioProducto.setText("");
            EspacioCantidad.setText("");
            EspacioPrecio.setText("");
            return;
        }
        ItemCarrito item = items.get(fila);
        EspacioProducto.setText(item.getProducto().getNombre());
        EspacioCantidad.setText(String.valueOf(item.getCantidad()));
        EspacioPrecio.setText(Moneda.pesos(item.getPrecioUnitario()));
    }

    private ItemCarrito seleccionado() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) {
            aviso("Selecciona un producto del carrito.", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return carrito.getItems().get(fila);
    }

    private void aviso(String mensaje, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, "Carrito", tipo);
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
        EspacioProducto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        EspacioCantidad = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        EspacioPrecio = new javax.swing.JTextField();
        BotonCambiarCantidad = new javax.swing.JButton();
        BotonQuitarProducto = new javax.swing.JButton();
        EspacioVaciar = new javax.swing.JButton();
        BotonCerrar = new javax.swing.JButton();
        BotonPagar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        jLabel1.setText("CARRITO");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio", "Subtotal"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("Producto");

        EspacioProducto.addActionListener(this::EspacioProductoActionPerformed);

        jLabel3.setText("Cantidad");

        EspacioCantidad.addActionListener(this::EspacioCantidadActionPerformed);

        jLabel4.setText("Precio");

        EspacioPrecio.addActionListener(this::EspacioPrecioActionPerformed);

        BotonCambiarCantidad.setText("CAMBIAR CANTIDAD");
        BotonCambiarCantidad.addActionListener(this::BotonCambiarCantidadActionPerformed);

        BotonQuitarProducto.setText("QUITAR PRODUCTO");
        BotonQuitarProducto.addActionListener(this::BotonQuitarProductoActionPerformed);

        EspacioVaciar.setText("VACIAR CARRITO");
        EspacioVaciar.addActionListener(this::EspacioVaciarActionPerformed);

        BotonCerrar.setText("CERRAR");
        BotonCerrar.addActionListener(this::BotonCerrarActionPerformed);

        BotonPagar.setText("PAGAR");
        BotonPagar.addActionListener(this::BotonPagarActionPerformed);

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
                        .addGap(179, 179, 179)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                        .addComponent(BotonCerrar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2)
                                .addGap(82, 82, 82)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(11, 11, 11)
                                    .addComponent(BotonCambiarCantidad)
                                    .addGap(48, 48, 48)
                                    .addComponent(BotonQuitarProducto))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(106, 106, 106)
                                    .addComponent(EspacioVaciar))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(EspacioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(48, 48, 48)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(BotonPagar)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(EspacioCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(48, 48, 48)
                                            .addComponent(EspacioPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(BotonCerrar)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(BotonPagar)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EspacioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EspacioCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EspacioPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BotonCambiarCantidad)
                    .addComponent(BotonQuitarProducto))
                .addGap(29, 29, 29)
                .addComponent(EspacioVaciar)
                .addGap(38, 38, 38))
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

    private void EspacioProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EspacioProductoActionPerformed
        // solo lectura: muestra el producto elegido en la tabla
    }//GEN-LAST:event_EspacioProductoActionPerformed

    private void BotonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_BotonCerrarActionPerformed

    private void BotonCambiarCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonCambiarCantidadActionPerformed
        ItemCarrito item = seleccionado();
        if (item == null) {
            return;
        }
        int nueva;
        try {
            nueva = Integer.parseInt(EspacioCantidad.getText().trim());
        } catch (NumberFormatException ex) {
            aviso("Escribe una cantidad válida (un número entero).", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nueva < 1) {
            aviso("La cantidad debe ser al menos 1. Para sacarlo del carrito usa QUITAR PRODUCTO.",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            carrito.cambiarCantidad(item.getProducto().getId(), nueva);
            refrescar();
        } catch (CarritoException ex) {
            aviso(ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_BotonCambiarCantidadActionPerformed

    private void BotonQuitarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonQuitarProductoActionPerformed
        ItemCarrito item = seleccionado();
        if (item == null) {
            return;
        }
        try {
            carrito.eliminar(item.getProducto().getId());
            refrescar();
        } catch (CarritoException ex) {
            aviso(ex.getMessage(), JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_BotonQuitarProductoActionPerformed

    private void EspacioVaciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EspacioVaciarActionPerformed
        if (carrito.estaVacio()) {
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Vaciar todo el carrito?", "Carrito",
                JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            carrito.vaciar();
            refrescar();
        }
    }//GEN-LAST:event_EspacioVaciarActionPerformed

    private void EspacioPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EspacioPrecioActionPerformed
        // solo lectura: muestra el precio del producto elegido en la tabla
    }//GEN-LAST:event_EspacioPrecioActionPerformed

    private void EspacioCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EspacioCantidadActionPerformed
        BotonCambiarCantidad.doClick(); // Enter en CANTIDAD = pulsar CAMBIAR CANTIDAD
    }//GEN-LAST:event_EspacioCantidadActionPerformed

    private void BotonPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonPagarActionPerformed
        if (carrito.estaVacio()) {
            aviso("Tu carrito está vacío. Agrega productos antes de pagar.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        PagoVista pago = new PagoVista(this, cliente, pedidos);
        pago.setVisible(true); // es modal: sigue aquí cuando se cierra
        if (pago.isCompraRealizada()) {
            alComprar.run(); // el catálogo recarga el stock
            dispose();       // el carrito quedó vacío
        } else {
            refrescar();
        }
    }//GEN-LAST:event_BotonPagarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonCambiarCantidad;
    private javax.swing.JButton BotonCerrar;
    private javax.swing.JButton BotonPagar;
    private javax.swing.JButton BotonQuitarProducto;
    private javax.swing.JTextField EspacioCantidad;
    private javax.swing.JTextField EspacioPrecio;
    private javax.swing.JTextField EspacioProducto;
    private javax.swing.JButton EspacioVaciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
