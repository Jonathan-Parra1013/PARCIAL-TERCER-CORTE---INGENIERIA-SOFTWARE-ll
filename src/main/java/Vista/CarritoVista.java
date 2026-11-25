package Vista;

import LogicaNegocio.Carrito.CarritoException;
import LogicaNegocio.Carrito.CarritoService;
import LogicaNegocio.Carrito.ItemCarrito;
import LogicaNegocio.Moneda;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;


public class CarritoVista extends JDialog {

    private final CarritoService carrito;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[][]{}, new String[]{"Producto", "Cant.", "Precio", "Subtotal"}) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final JLabel etiquetaSubtotal = new JLabel("", SwingConstants.RIGHT);

    public CarritoVista(Frame padre, CarritoService carrito) {
        super(padre, "Carrito", true);
        this.carrito = carrito;

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        JButton botonCantidad = new JButton("CAMBIAR CANTIDAD");
        JButton botonQuitar = new JButton("QUITAR");
        JButton botonVaciar = new JButton("VACIAR");
        JButton botonCerrar = new JButton("CERRAR");
        botonCantidad.addActionListener(e -> cambiarCantidad());
        botonQuitar.addActionListener(e -> quitar());
        botonVaciar.addActionListener(e -> vaciar());
        botonCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        botones.setOpaque(false);
        botones.add(botonCantidad);
        botones.add(botonQuitar);
        botones.add(botonVaciar);
        botones.add(botonCerrar);

        JPanel abajo = new JPanel(new BorderLayout(0, 10));
        abajo.setOpaque(false);
        abajo.add(etiquetaSubtotal, BorderLayout.NORTH);
        abajo.add(botones, BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(204, 255, 204));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(new JLabel("CARRITO", SwingConstants.CENTER), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new java.awt.Dimension(520, 240));
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(abajo, BorderLayout.SOUTH);

        setContentPane(panel);
        pack();
        setLocationRelativeTo(padre);
        refrescar();
    }

    private void refrescar() {
        modelo.setRowCount(0);
        for (ItemCarrito item : carrito.getItems()) {
            modelo.addRow(new Object[]{
                item.getProducto().getNombre(),
                item.getCantidad(),
                Moneda.pesos(item.getPrecioUnitario()),
                Moneda.pesos(item.getSubtotal())});
        }
        etiquetaSubtotal.setText(carrito.estaVacio()
                ? "El carrito está vacío"
                : "Subtotal: " + Moneda.pesos(carrito.getSubtotal()));
    }

    private ItemCarrito seleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto del carrito.",
                    "Carrito", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return carrito.getItems().get(fila);
    }

    private void cambiarCantidad() {
        ItemCarrito item = seleccionado();
        if (item == null) {
            return;
        }
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(item.getCantidad(), 1, 999, 1));
        int r = JOptionPane.showConfirmDialog(this,
                new Object[]{"Nueva cantidad de \"" + item.getProducto().getNombre() + "\":", spinner},
                "Cambiar cantidad", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            spinner.commitEdit();
            carrito.cambiarCantidad(item.getProducto().getId(), ((Number) spinner.getValue()).intValue());
            refrescar();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad no válida.", "Carrito", JOptionPane.WARNING_MESSAGE);
        } catch (CarritoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Carrito", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void quitar() {
        ItemCarrito item = seleccionado();
        if (item == null) {
            return;
        }
        try {
            carrito.eliminar(item.getProducto().getId());
            refrescar();
        } catch (CarritoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Carrito", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void vaciar() {
        if (carrito.estaVacio()) {
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Vaciar todo el carrito?", "Carrito",
                JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            carrito.vaciar();
            refrescar();
        }
    }
}
