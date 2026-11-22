package BaseDatos;

/**
 *
 * @author jonap
 */


import LogicaNegocio.DetallePedido;
import LogicaNegocio.EstadoPedido;
import LogicaNegocio.MedioPago;
import LogicaNegocio.Pedido;
import LogicaNegocio.PedidoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class PedidoDAO {

    // el subtotal no es una columna: se calcula sumando los detalles del pedido
    private static final String SELECT_PEDIDO =
            "SELECT p.id_pedido, p.id_cliente, p.fecha_pedido, p.metodo_pago, p.estado, "
          + "p.direccion_envio, p.total, "
          + "(SELECT COALESCE(SUM(d.subtotal), 0) FROM detalle_pedidos d WHERE d.id_pedido = p.id_pedido) AS subtotal "
          + "FROM pedidos p ";

    private final Connection con;
    private final ProductoDAO productoDAO = new ProductoDAO();

    public PedidoDAO() {
        this.con = Conexion.getInstancia().getConexion();
    }

    public int crear(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (id_cliente, fecha_pedido, metodo_pago, estado, direccion_envio, total) "
                         + "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario_aplicado, subtotal) "
                          + "VALUES (?, ?, ?, ?, ?)";


        List<DetallePedido> ordenados = new ArrayList<>(pedido.getDetalles());
        ordenados.sort(Comparator.comparingInt(DetallePedido::getIdProducto));

        boolean autoCommitOriginal = true;
        try {
            autoCommitOriginal = con.getAutoCommit();
            con.setAutoCommit(false);

            int idPedido;
            try (PreparedStatement ps = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, pedido.getIdCliente());
                ps.setTimestamp(2, Timestamp.valueOf(pedido.getFecha()));
                ps.setString(3, pedido.getMedioPago().name());
                ps.setString(4, pedido.getEstado().name());
                ps.setString(5, pedido.getDireccionEnvio());
                ps.setBigDecimal(6, pedido.getTotal());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("No se obtuvo el número del pedido.");
                    }
                    idPedido = keys.getInt(1);
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                for (DetallePedido d : ordenados) {
                    if (!productoDAO.descontarStock(con, d.getIdProducto(), d.getCantidad())) {
                        throw new PedidoException("\"" + d.getNombreProducto()
                                + "\" ya no está disponible o no tiene stock suficiente. "
                                + "Ajusta tu carrito e inténtalo de nuevo.");
                    }
                    ps.setInt(1, idPedido);
                    ps.setInt(2, d.getIdProducto());
                    ps.setInt(3, d.getCantidad());
                    ps.setBigDecimal(4, d.getPrecioUnitario());
                    ps.setBigDecimal(5, d.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            con.commit();
            return idPedido;

        } catch (PedidoException e) {
            deshacer();
            throw e;
        } catch (SQLException e) {
            deshacer();
            throw new PedidoException("No se pudo guardar el pedido: " + e.getMessage());
        } finally {
            try {
                con.setAutoCommit(autoCommitOriginal);
            } catch (SQLException e) {
                System.err.println("Error restaurando autocommit: " + e.getMessage());
            }
        }
    }

    private void deshacer() {
        try {
            con.rollback();
        } catch (SQLException e) {
            System.err.println("Error en rollback: " + e.getMessage());
        }
    }

    public List<Pedido> listarPorCliente(int idCliente) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = SELECT_PEDIDO + "WHERE p.id_cliente = ? ORDER BY p.fecha_pedido DESC, p.id_pedido DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapear(rs, List.of()));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error listando pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    public Pedido buscarPorId(int idPedido) {
        String sql = SELECT_PEDIDO + "WHERE p.id_pedido = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs, cargarDetalles(idPedido));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error buscando pedido: " + e.getMessage());
        }
        return null;
    }


    public int contarPorCliente(int idCliente) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE id_cliente = ? AND estado <> 'CANCELADO'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error contando pedidos: " + e.getMessage());
        }
        return 0;
    }

    private List<DetallePedido> cargarDetalles(int idPedido) throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT d.id_producto, p.nombre, d.cantidad, d.precio_unitario_aplicado "
                   + "FROM detalle_pedidos d JOIN productos p ON d.id_producto = p.id_producto "
                   + "WHERE d.id_pedido = ? ORDER BY d.id_detalle";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new DetallePedido(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad"),
                            rs.getBigDecimal("precio_unitario_aplicado")));
                }
            }
        }
        return detalles;
    }

    private Pedido mapear(ResultSet rs, List<DetallePedido> detalles) throws SQLException {
        return new Pedido(
                rs.getInt("id_pedido"),
                rs.getInt("id_cliente"),
                rs.getTimestamp("fecha_pedido").toLocalDateTime(),
                detalles,
                rs.getBigDecimal("subtotal"),
                rs.getBigDecimal("total"),
                MedioPago.valueOf(rs.getString("metodo_pago")),
                EstadoPedido.valueOf(rs.getString("estado")),
                rs.getString("direccion_envio"));
    }
}
