package BaseDatos;

import LogicaNegocio.Reportes.ProductoVendido;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Consultas de ventas. Supone estas tablas (ajusta los nombres a tu base):
 *   pedidos(id_pedido, fecha, total)
 *   detalle_pedido(id_pedido, id_producto, cantidad, precio_unitario)
 *   productos(id_producto, nombre)
 *
 * Si no hay ventas, las consultas devuelven 0 / 0.00 / lista vacía (nunca null).
 * Si falla la base de datos se lanza una excepción: así un error real no se
 * confunde con "no hubo ventas".
 */
public class VentaDAO {

    private Connection conexion() {
        Connection con = Conexion.getInstancia().getConexion();
        if (con == null) {
            throw new IllegalStateException("No hay conexión con la base de datos");
        }
        return con;
    }

    public int contarVentas(LocalDateTime desde, LocalDateTime hasta) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE fecha >= ? AND fecha < ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(desde));
            ps.setTimestamp(2, Timestamp.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error contando las ventas: " + e.getMessage(), e);
        }
    }

    public BigDecimal sumarIngresos(LocalDateTime desde, LocalDateTime hasta) {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM pedidos WHERE fecha >= ? AND fecha < ?";
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(desde));
            ps.setTimestamp(2, Timestamp.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal(1);
                    return total == null ? BigDecimal.ZERO : total;
                }
                return BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error sumando los ingresos: " + e.getMessage(), e);
        }
    }

    /** Productos vendidos en el rango, de mayor a menor cantidad. */
    public List<ProductoVendido> productosVendidos(LocalDateTime desde, LocalDateTime hasta) {
        String sql = "SELECT p.nombre, SUM(d.cantidad) AS unidades, "
                + "SUM(d.cantidad * d.precio_unitario) AS ingresos "
                + "FROM detalle_pedido d "
                + "JOIN pedidos pe ON pe.id_pedido = d.id_pedido "
                + "JOIN productos p ON p.id_producto = d.id_producto "
                + "WHERE pe.fecha >= ? AND pe.fecha < ? "
                + "GROUP BY p.id_producto, p.nombre "
                + "ORDER BY unidades DESC, ingresos DESC";
        List<ProductoVendido> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(desde));
            ps.setTimestamp(2, Timestamp.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ProductoVendido(
                            rs.getString("nombre"),
                            rs.getLong("unidades"),
                            rs.getBigDecimal("ingresos")));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error consultando productos vendidos: " + e.getMessage(), e);
        }
        return lista;
    }
}
