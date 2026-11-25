/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author jonap
 */
public class Conexion {

   
    private static Conexion instancia;
    private Connection conexion;
    private String ultimoError; // por qué falló la conexión (null si todo bien)

    // datos para la conexion, de la base de datos local
    private static final String URL = "jdbc:mysql://localhost:3306/tienda_online_db?useSSL=false&serverTimezone=America/Bogota";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // En XAMPP por defecto viene vacío

    // singleton :D
    private Conexion() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            // Esta clase es de la capa de datos: no abre ventanas. El mensaje lo muestra la pantalla
            // (UsuarioService lo convierte) usando getUltimoError().
            ultimoError = e.getMessage();
            System.err.println("Error al conectar con MySQL (XAMPP): " + e.getMessage());
        }
    }

    // acceso a la unica estancia :b
    public static synchronized Conexion getInstancia() {
        try {
            if (instancia == null || instancia.getConexion() == null || instancia.getConexion().isClosed()) {
                instancia = new Conexion();
            }
        } catch (SQLException e) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    /** Motivo del último intento de conexión fallido; null si se conectó bien. */
    public String getUltimoError() {
        return ultimoError;
    }
}