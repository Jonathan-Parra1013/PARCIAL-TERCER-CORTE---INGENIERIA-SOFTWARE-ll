package LogicaNegocio.Producto;

import LogicaNegocio.Producto.Producto;
import LogicaNegocio.Producto.ProductoException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FormularioProducto extends JPanel {
    private final JTextField campoNombre = new JTextField(22);
    private final JComboBox<String> campoCategoria;
    private final JTextField campoPrecio = new JTextField(22);
    private final JTextField campoStock = new JTextField(22);
    private final JTextArea campoDescripcion = new JTextArea(3, 22);
    private final JTextField campoImagen = new JTextField(16);


    public FormularioProducto(List<String> categorias, Producto base) {
        super(new GridBagLayout());
        campoCategoria = new JComboBox<>(categorias.toArray(new String[0]));
        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);

        if (base != null) {
            campoNombre.setText(base.getNombre());
            campoCategoria.setSelectedItem(base.getCategoria());
            campoPrecio.setText(base.getPrecio().stripTrailingZeros().toPlainString());
            campoStock.setText(String.valueOf(base.getStock()));
            campoDescripcion.setText(base.getDescripcion() == null ? "" : base.getDescripcion());
            campoImagen.setText(base.getRutaImagen() == null ? "" : base.getRutaImagen());
        }

        JButton examinar = new JButton("Examinar...");
        examinar.addActionListener(e -> elegirImagen());
        JPanel filaImagen = new JPanel(new BorderLayout(5, 0));
        filaImagen.add(campoImagen, BorderLayout.CENTER);
        filaImagen.add(examinar, BorderLayout.EAST);

        agregarFila(0, "Nombre", campoNombre);
        agregarFila(1, "Categoría", campoCategoria);
        agregarFila(2, "Precio", campoPrecio);
        agregarFila(3, "Stock", campoStock);
        agregarFila(4, "Descripción", new JScrollPane(campoDescripcion));
        agregarFila(5, "Imagen", filaImagen);
    }

    private void agregarFila(int fila, String etiqueta, Component campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila;
        c.insets = new Insets(3, 3, 3, 3);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(campo, c);
    }

    private void elegirImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "png", "jpg", "jpeg", "gif"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            campoImagen.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public String nombre() { return campoNombre.getText(); }
    public String categoria() { return (String) campoCategoria.getSelectedItem(); }
    public String descripcion() { return campoDescripcion.getText().trim(); }
    public String imagen() { return campoImagen.getText().trim(); }

    // solo dígitos (y punto para decimales): "50.000" se rechaza para no guardar $50 por error
    public BigDecimal precio() {
        String t = campoPrecio.getText().trim();
        if (!t.matches("\\d+(\\.\\d{1,2})?")) {
            throw new ProductoException("Escribe el precio solo con números, sin separador de miles "
                    + "(por ejemplo 15000 o 15000.50).");
        }
        return new BigDecimal(t);
    }

    public int stock() {
        String t = campoStock.getText().trim();
        try {
            if (!t.matches("\\d+")) {
                throw new NumberFormatException();
            }
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            throw new ProductoException("El stock debe ser un número entero, 0 o mayor.");
        }
    }
}
