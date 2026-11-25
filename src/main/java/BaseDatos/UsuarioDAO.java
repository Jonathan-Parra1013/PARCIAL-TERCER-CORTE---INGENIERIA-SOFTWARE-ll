/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BaseDatos;

/**
 *
 * @author jonap
 */


import LogicaNegocio.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;


public class UsuarioDAO implements LogicaNegocio.UsuarioRepositorio {

    private final Connection con;

    public UsuarioDAO() {
        this.con = Conexion.getInstancia().getConexion();
    }

    public boolean existeCorreo(String correo) {
        String sql = "SELECT id_usuario FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error verificando correo: " + e.getMessage());
            return false;
        }
    }

    // creacion de cuenta de un usuario y se cifra la contraseña :b
    public boolean registrarUsuario(Usuario usuario) {
        if (existeCorreo(usuario.getEmail())) {
            return false;
        }
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            String hash = BCrypt.hashpw(usuario.getPasswordHash(), BCrypt.gensalt());
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, hash);
            ps.setString(4, usuario.getRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    // inicio de sesion de cuentass
    public Usuario iniciarSesion(String correo, String contrasenaPlana) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGuardado = rs.getString("contrasena");
                    // Soporta tanto contraseñas cifradas con BCrypt como texto plano de prueba inicial
                    boolean coincide = hashGuardado.startsWith("$2a$") 
                            ? BCrypt.checkpw(contrasenaPlana, hashGuardado)
                            : contrasenaPlana.equals(hashGuardado);

                    if (coincide) {
                        return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            hashGuardado,
                            rs.getString("rol")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en inicio de sesión: " + e.getMessage());
        }
        return null;
    }
}
