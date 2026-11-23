package GUI;

import BaseDatos.UsuarioDAO;
import LogicaNegocio.Usuario;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RegistroFrame extends JFrame {

    // Deben coincidir con los valores que guardas en la columna "rol" de la base de datos
    private static final String ROL_CLIENTE = "CLIENTE";
    private static final String ROL_VENDEDOR = "VENDEDOR";

    private final LoginFrame login;

    private final JTextField txtNombre = new JTextField(20);
    private final JTextField txtCorreo = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JPasswordField txtConfirmar = new JPasswordField(20);
    private final char echoPorDefecto = txtPassword.getEchoChar();
    private final JCheckBox chkMostrar = new JCheckBox("Mostrar contraseñas");
    private final JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Cliente", "Vendedor"});
    private final JButton btnRegistrar = new JButton("Crear cuenta");
    private final JButton btnCancelar = new JButton("Volver");

    public RegistroFrame(LoginFrame login) {
        super("Tienda online - Crear cuenta");
        this.login = login;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirInterfaz();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);

        // Al cerrarse esta ventana (por la X, "Volver" o tras registrar) reaparece el login
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                RegistroFrame.this.login.setVisible(true);
            }
        });
    }

    private void construirInterfaz() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Crear cuenta", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(titulo, c);

        c.gridwidth = 1;
        agregarFila(panel, c, 1, "Nombre:", txtNombre);
        agregarFila(panel, c, 2, "Correo:", txtCorreo);
        agregarFila(panel, c, 3, "Contraseña:", txtPassword);
        agregarFila(panel, c, 4, "Confirmar:", txtConfirmar);
        agregarFila(panel, c, 5, "Soy:", cmbRol);

        c.gridy = 6; c.gridx = 1;
        panel.add(chkMostrar, c);

        JPanel botones = new JPanel();
        botones.add(btnRegistrar);
        botones.add(btnCancelar);
        c.gridy = 7; c.gridx = 0; c.gridwidth = 2;
        panel.add(botones, c);

        setContentPane(panel);
        getRootPane().setDefaultButton(btnRegistrar);

        chkMostrar.addActionListener(e -> {
            char eco = chkMostrar.isSelected() ? (char) 0 : echoPorDefecto;
            txtPassword.setEchoChar(eco);
            txtConfirmar.setEchoChar(eco);
        });
        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void agregarFila(JPanel panel, GridBagConstraints c, int fila, String etiqueta,
                             java.awt.Component campo) {
        c.gridy = fila; c.gridx = 0; c.weightx = 0;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(campo, c);
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim().toLowerCase();
        String clave = new String(txtPassword.getPassword());
        String confirmacion = new String(txtConfirmar.getPassword());
        String rol = cmbRol.getSelectedIndex() == 0 ? ROL_CLIENTE : ROL_VENDEDOR;

        String error = validar(nombre, correo, clave, confirmacion);
        if (error != null) {
            mensaje(error, JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            if (dao.existeCorreo(correo)) {
                mensaje("Ya existe una cuenta con ese correo.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // El DAO cifra la contraseña con BCrypt antes de guardarla
            Usuario nuevo = new Usuario(0, nombre, correo, clave, rol);
            if (dao.registrarUsuario(nuevo)) {
                mensaje("Cuenta creada. Ya puedes iniciar sesión.", JOptionPane.INFORMATION_MESSAGE);
                login.setCorreo(correo);
                dispose();
            } else {
                mensaje("No se pudo crear la cuenta. Revisa la conexión con la base de datos.",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            mensaje("No se pudo conectar con la base de datos.\n" + ex.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String validar(String nombre, String correo, String clave, String confirmacion) {
        if (nombre.isEmpty()) return "El nombre es obligatorio.";
        if (!correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) return "Ingresa un correo válido.";
        if (clave.length() < 8) return "La contraseña debe tener al menos 8 caracteres.";
        if (!clave.equals(confirmacion)) return "Las contraseñas no coinciden.";
        return null;
    }

    private void mensaje(String texto, int tipo) {
        JOptionPane.showMessageDialog(this, texto, "Tienda online", tipo);
    }
}
