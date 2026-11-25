/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import LogicaNegocio.Carrito.CarritoException;
import LogicaNegocio.Carrito.CarritoService;
import LogicaNegocio.Moneda;
import LogicaNegocio.Producto.Producto;
import LogicaNegocio.Producto.ProductoService;
import LogicaNegocio.Usuario;
import LogicaNegocio.UsuarioService;
import LogicaNegocio.Ventanas;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jonap
 */
public class ClienteVista extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ClienteVista.class.getName());

    private static final String TODAS = "Todas";

    private final Usuario cliente;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final CarritoService carritoService;

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[][]{}, new String[]{"PRODUCTO", "STOCK", "PRECIO"}) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private List<Producto> productos = new ArrayList<>();
    private CarritoVista ventanaCarrito; // la ventana del carrito (una sola a la vez)

    public ClienteVista(Usuario cliente, UsuarioService usuarioService,
                        ProductoService productoService, CarritoService carritoService) {
        this.cliente = cliente;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.carritoService = carritoService;
        initComponents();
        configurar();
        setLocationRelativeTo(null);
        cargarProductos();
    }

    // Todo lo que se agrega a mano va fuera de initComponents() para que el
    // editor de NetBeans no lo borre al regenerar el formulario.
    private void configurar() {
        setTitle("Cliente - " + cliente.getNombre());

        jTable1.setModel(modelo);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarSeleccion();
            }
        });

        // los campos PRODUCTO / PRECIO se llenan al elegir una fila de la tabla
        EspacioProducto.setEditable(false);
        EspacioPrecio.setEditable(false);

        List<String> opciones = new ArrayList<>();
        opciones.add(TODAS);
        opciones.addAll(productoService.categorias());
        FiltroProductos.setModel(new DefaultComboBoxModel<>(opciones.toArray(new String[0])));
        FiltroProductos.addActionListener(e -> cargarProductos());

        CarritoBoton.addActionListener(e -> abrirCarrito());
        AGREGARCARRITO.addActionListener(this::AgregarCarritoActionPerformed);
    }

    // El carrito es una ventana propia y no modal: se abre una sola, y si ya está abierta se trae al frente
    private void abrirCarrito() {
        if (ventanaCarrito == null || !ventanaCarrito.isDisplayable()) {
            ventanaCarrito = new CarritoVista(this, carritoService);
        }
        ventanaCarrito.setVisible(true);
        ventanaCarrito.toFront();
    }

    // al cerrar sesión o cerrar esta ventana no debe quedar el carrito abierto
    @Override
    public void dispose() {
        if (ventanaCarrito != null) {
            ventanaCarrito.dispose();
        }
        super.dispose();
    }

    private void cargarProductos() {
        String categoria = (String) FiltroProductos.getSelectedItem();
        if (categoria == null || TODAS.equals(categoria)) {
            categoria = null;
        }
        productos = productoService.catalogo(categoria);
        modelo.setRowCount(0);
        for (Producto p : productos) {
            modelo.addRow(new Object[]{p.getNombre(), p.getStock(), Moneda.pesos(p.getPrecio())});
        }
        mostrarSeleccion();
    }

    private void mostrarSeleccion() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0 || fila >= productos.size()) {
            EspacioProducto.setText("");
            EspacioPrecio.setText("");
            return;
        }
        Producto p = productos.get(fila);
        EspacioProducto.setText(p.getNombre());
        EspacioPrecio.setText(Moneda.pesos(p.getPrecio()));
    }

    private void AgregarCarritoActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.",
                    "Tienda online", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Producto producto = productos.get(fila);
        JSpinner cantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        int r = JOptionPane.showConfirmDialog(this,
                new Object[]{"¿Cuántas unidades de \"" + producto.getNombre() + "\"?", cantidad},
                "Agregar al carrito", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            cantidad.commitEdit();
            carritoService.agregar(producto, ((Number) cantidad.getValue()).intValue());
            JOptionPane.showMessageDialog(this, "Producto agregado al carrito.",
                    "Tienda online", JOptionPane.INFORMATION_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad no válida.",
                    "Tienda online", JOptionPane.WARNING_MESSAGE);
        } catch (CarritoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Tienda online", JOptionPane.WARNING_MESSAGE);
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
        SalirBoton = new javax.swing.JButton();
        FiltroProductos = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        CarritoBoton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        EspacioProducto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        EspacioPrecio = new javax.swing.JTextField();
        AGREGARCARRITO = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        jLabel1.setText("CLIENTE");

        SalirBoton.setText("SALIR");
        SalirBoton.addActionListener(this::SalirBotonActionPerformed);

        FiltroProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Filtro");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "PRODUCTO", "STOCK", "PRECIO"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        CarritoBoton.setText("CARRITO");

        jLabel4.setText("PRODUCTO");

        jLabel5.setText("PRECIO");

        EspacioPrecio.addActionListener(this::EspacioPrecioActionPerformed);

        AGREGARCARRITO.setText("AGREGAR CARRITO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SalirBoton)
                .addGap(98, 98, 98)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(CarritoBoton)
                .addGap(16, 16, 16))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel4)
                                .addGap(57, 57, 57)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(EspacioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(EspacioPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(26, 26, 26)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(FiltroProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(169, 169, 169)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(26, 26, 26)
                                .addComponent(AGREGARCARRITO)))
                        .addGap(0, 28, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(SalirBoton)
                    .addComponent(CarritoBoton))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FiltroProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EspacioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EspacioPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AGREGARCARRITO))
                .addContainerGap(67, Short.MAX_VALUE))
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

    private void SalirBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirBotonActionPerformed
        Ventanas.cerrarSesion(this, usuarioService);
    }//GEN-LAST:event_SalirBotonActionPerformed

    private void EspacioPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EspacioPrecioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EspacioPrecioActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AGREGARCARRITO;
    private javax.swing.JButton CarritoBoton;
    private javax.swing.JTextField EspacioPrecio;
    private javax.swing.JTextField EspacioProducto;
    private javax.swing.JComboBox<String> FiltroProductos;
    private javax.swing.JButton SalirBoton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
