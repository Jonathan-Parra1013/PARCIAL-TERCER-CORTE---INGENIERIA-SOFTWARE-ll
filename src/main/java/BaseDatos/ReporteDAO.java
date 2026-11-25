package BaseDatos;

import LogicaNegocio.PagosStrategy.MedioPago;
import LogicaNegocio.Pedido.EstadoPedido;
import LogicaNegocio.Reportes.LineaVenta;
import LogicaNegocio.Reportes.ReporteException;
import LogicaNegocio.Reportes.VentasRepositorio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO implements VentasRepositorio {

    private final Connection con;

    public ReporteDAO() {
        this.con = Conexion.getInstancia().getConexion();
    }

    @Override
    public List<LineaVenta> listarVentasDelDia(int idVendedor, LocalDate dia) {
        String sql = "SELECT p.id_pedido, p.fecha_pedido, u.nombre AS cliente, p.metodo_pago, p.estado, "
                   + "pr.nombre AS producto, d.cantidad, d.precio_unitario_aplicado, d.subtotal "
                   + "FROM detalle_pedidos d "
                   + "JOIN pedidos p ON d.id_pedido = p.id_pedido "
                   + "JOIN productos pr ON d.id_producto = pr.id_producto "
                   + "JOIN usuarios u ON p.id_cliente = u.id_usuario "
                   + "WHERE pr.id_vendedor = ? AND p.estado <> 'CANCELADO' "
                   + "AND p.fecha_pedido >= ? AND p.fecha_pedido < ? "
                   + "ORDER BY p.fecha_pedido, p.id_pedido, d.id_detalle";
        List<LineaVenta> lineas = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVendedor);
            ps.setTimestamp(2, Timestamp.valueOf(dia.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(dia.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lineas.add(new LineaVenta(
                            rs.getInt("id_pedido"),
                            rs.getTimestamp("fecha_pedido").toLocalDateTime(),
                            rs.getString("cliente"),
                            MedioPago.valueOf(rs.getString("metodo_pago")),
                            EstadoPedido.valueOf(rs.getString("estado")),
                            rs.getString("producto"),
                            rs.getInt("cantidad"),
                            rs.getBigDecimal("precio_unitario_aplicado"),
                            rs.getBigDecimal("subtotal")));
                }
            }
        } catch (SQLException e) {
            throw new ReporteException("No se pudo consultar las ventas: " + e.getMessage());
        }
        return lineas;
    }
}
