package GUI;

import BaseDatos.UsuarioDAO;
import LogicaNegocio.Usuario;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginFrame extends JFrame {

    private final JTextField txtCorreo = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final char echoPorDefecto = txtPassword.getEchoChar();
    private final JCheckBox chkMostrar = new JCheckBox("Mostrar contraseña");
    private final JButton btnIngresar = new JButton("Iniciar sesión");
    private final JButton btnRegistro = new JButton("¿No tienes cuenta? Regístrate");

    public LoginFrame() {

        super("Tienda online - Iniciar sesión");
        System.out.println("Iniciando login frame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        construirInterfaz();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Iniciar sesión", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(titulo, c);

        c.gridwidth = 1;
        c.gridy = 1; c.gridx = 0; c.weightx = 0;
        panel.add(new JLabel("Correo:"), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(txtCorreo, c);

        c.gridy = 2; c.gridx = 0; c.weightx = 0;
        panel.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(txtPassword, c);

        c.gridy = 3; c.gridx = 1;
        panel.add(chkMostrar, c);

        c.gridy = 4; c.gridx = 0; c.gridwidth = 2;
        panel.add(btnIngresar, c);

        // El botón de registro se ve como un enlace
        btnRegistro.setBorderPainted(false);
        btnRegistro.setContentAreaFilled(false);
        btnRegistro.setForeground(new Color(0, 102, 204));
        btnRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.gridy = 5;
        panel.add(btnRegistro, c);

        setContentPane(panel);
        getRootPane().setDefaultButton(btnIngresar); // Enter = iniciar sesión

        chkMostrar.addActionListener(e ->
                txtPassword.setEchoChar(chkMostrar.isSelected() ? (char) 0 : echoPorDefecto));
        btnIngresar.addActionListener(e -> iniciarSesion());
        btnRegistro.addActionListener(e -> {
            setVisible(false);
            new RegistroFrame(this).setVisible(true);
        });
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim().toLowerCase();
        char[] clave = txtPassword.getPassword();

        if (correo.isEmpty() || clave.length == 0) {
            mensaje("Ingresa tu correo y tu contraseña.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = new UsuarioDAO().iniciarSesion(correo, new String(clave));
            if (usuario == null) {
                mensaje("Correo o contraseña incorrectos.", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
            } else {
                abrirVentanaPrincipal(usuario);
            }
        } catch (Exception ex) {
            mensaje("No se pudo conectar con la base de datos.\n" + ex.getMessage(),
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            java.util.Arrays.fill(clave, '\0');
        }
    }

    /** Punto de enlace con el resto de tu aplicación. */
    private void abrirVentanaPrincipal(Usuario usuario) {
        JOptionPane.showMessageDialog(this,
                "Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        // TODO: abre aquí tu ventana principal según el rol y luego cierra el login:
        // if ("VENDEDOR".equalsIgnoreCase(usuario.getRol())) {
        //     new PanelVendedorFrame(usuario).setVisible(true);
        // } else {
        //     new TiendaFrame(usuario).setVisible(true);
        // }
        // dispose();
    }

    /** Lo usa RegistroFrame para dejar el correo ya escrito tras crear la cuenta. */
    public void setCorreo(String correo) {
        txtCorreo.setText(correo);
        txtPassword.setText("");
    }

    private void mensaje(String texto, int tipo) {
        JOptionPane.showMessageDialog(this, texto, "Tienda online", tipo);
    }
}
