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

    // La conexión se pide en cada consulta y no al crear el DAO: así la ventana abre aunque MySQL
    // esté apagado, y si se enciende después el siguiente intento funciona sin reiniciar la aplicación.
    private Connection con() {
        Conexion conexion = Conexion.getInstancia();
        Connection c = conexion.getConexion();
        if (c == null) {
            throw new LogicaNegocio.BaseDatosException("No se pudo conectar con la base de datos.\n"
                    + "Verifica que MySQL (XAMPP) esté encendido y que exista tienda_online_db.\n"
                    + "Detalle: " + conexion.getUltimoError());
        }
        return c;
    }

    public boolean existeCorreo(String correo) {
        Connection con = con();
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
        Connection con = con();
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
        Connection con = con();
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
