package LogicaNegocio;

import BaseDatos.ProductoDAO;
import BaseDatos.ReporteDAO;
import LogicaNegocio.Carrito.CarritoService;
import LogicaNegocio.Descuentos.CalculadoraDescuentos;
import LogicaNegocio.Descuentos.DescuentoClienteFrecuente;
import LogicaNegocio.Descuentos.DescuentoPorCupon;
import LogicaNegocio.Descuentos.DescuentoPorMonto;
import LogicaNegocio.Descuentos.DescuentoPorVolumen;
import LogicaNegocio.Producto.ProductoService;
import LogicaNegocio.Reportes.ReporteService;
import LogicaNegocio.Usuario;
import LogicaNegocio.UsuarioService;
import Vista.ClienteVista;
import Vista.DuenoVista;
import Vista.IngresarVista;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public final class Ventanas {

    private Ventanas() { }


    public static boolean abrirPrincipal(Usuario usuario, UsuarioService usuarioService) {
        JFrame ventana;
        if (UsuarioService.ROL_VENDEDOR.equalsIgnoreCase(usuario.getRol())) {
            ventana = new DuenoVista(usuario, usuarioService,
                    new ProductoService(new ProductoDAO()),
                    new ReporteService(new ReporteDAO()));
        } else if (UsuarioService.ROL_CLIENTE.equalsIgnoreCase(usuario.getRol())) {
            ventana = new ClienteVista(usuario, usuarioService,
                    new ProductoService(new ProductoDAO()),
                    new CarritoService(crearCalculadoraDescuentos()));
        } else {
            JOptionPane.showMessageDialog(null, "Tu cuenta tiene un rol sin pantalla asignada: " + usuario.getRol(),
                    "Tienda online", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        return true;
    }


    public static void cerrarSesion(JFrame actual, UsuarioService usuarioService) {
        IngresarVista login = new IngresarVista(usuarioService);
        login.setVisible(true);
        actual.dispose();
    }

    private static CalculadoraDescuentos crearCalculadoraDescuentos() {
        return new CalculadoraDescuentos(
                List.of(new DescuentoPorVolumen(),
                        new DescuentoClienteFrecuente(),
                        new DescuentoPorMonto(),
                        new DescuentoPorCupon(Map.of("BIENVENIDO10", new BigDecimal("10")))),
                new BigDecimal("25"));
    }
}
