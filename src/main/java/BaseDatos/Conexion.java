/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author jonap
 */
public class Conexion {

   
    private static Conexion instancia;
    private Connection conexion;

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
            JOptionPane.showMessageDialog(null, 
                "Error al conectar con MySQL (XAMPP):\n" + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
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
}