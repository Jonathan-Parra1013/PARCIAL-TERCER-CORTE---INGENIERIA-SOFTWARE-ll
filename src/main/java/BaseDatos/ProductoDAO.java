package BaseDatos;
/**
 *
 * @author jonap
 */


import LogicaNegocio.Producto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductoDAO {

    private static final String SELECT_BASE =
            "SELECT p.id_producto, p.nombre, p.descripcion, p.precio, p.stock, p.ruta_imagen, "
          + "p.id_vendedor, p.activo, c.nombre AS categoria "
          + "FROM productos p JOIN categorias c ON p.id_categoria = c.id_categoria ";

    private final Connection con;

    public ProductoDAO() {
        this.con = Conexion.getInstancia().getConexion();
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        return new Producto.Builder(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getBigDecimal("precio"),
                rs.getInt("stock"))
                .descripcion(rs.getString("descripcion"))
                .categoria(rs.getString("categoria"))
                .idVendedor(rs.getInt("id_vendedor"))
                .rutaImagen(rs.getString("ruta_imagen"))
                .activo(rs.getBoolean("activo"))
                .build();
    }

    // catálogo completo (solo productos activos)
    public List<Producto> listarTodos() {
        return filtrar(null, null, null, null);
    }

    public List<Producto> filtrar(String categoria, BigDecimal precioMin,
                                  BigDecimal precioMax, String texto) {
        StringBuilder sql = new StringBuilder(SELECT_BASE).append("WHERE p.activo = 1 ");
        List<Object> params = new ArrayList<>();

        if (categoria != null && !categoria.isBlank()) {
            sql.append("AND c.nombre = ? ");
            params.add(categoria.trim());
        }
        if (precioMin != null) {
            sql.append("AND p.precio >= ? ");
            params.add(precioMin);
        }
        if (precioMax != null) {
            sql.append("AND p.precio <= ? ");
            params.add(precioMax);
        }
        if (texto != null && !texto.isBlank()) {
            sql.append("AND p.nombre LIKE ? ");
            params.add("%" + texto.trim() + "%");
        }
        sql.append("ORDER BY p.nombre");

        List<Producto> resultado = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error filtrando productos: " + e.getMessage());
        }
        return resultado;
    }

    // null si no existe :D 
    public Producto buscarPorId(int idProducto) {
        String sql = SELECT_BASE + "WHERE p.id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error buscando producto: " + e.getMessage());
        }
        return null;
    }

  
    public boolean crear(Producto producto) {
        producto.validarParaPublicar();
        String sql = "INSERT INTO productos (id_categoria, id_vendedor, nombre, descripcion, precio, stock, ruta_imagen) "
                   + "VALUES ((SELECT id_categoria FROM categorias WHERE nombre = ?), ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getCategoria());
            ps.setInt(2, producto.getIdVendedor());
            ps.setString(3, producto.getNombre());
            ps.setString(4, producto.getDescripcion());
            ps.setBigDecimal(5, producto.getPrecio());
            ps.setInt(6, producto.getStock());
            ps.setString(7, producto.getRutaImagen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear producto: " + e.getMessage());
            return false;
        }
    }

    public List<String> listarCategorias() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT nombre FROM categorias ORDER BY nombre";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categorias.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error listando categorías: " + e.getMessage());
        }
        return categorias;
    }


    public boolean descontarStock(Connection conTransaccion, int idProducto, int cantidad) throws SQLException {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id_producto = ? AND activo = 1 AND stock >= ?";
        try (PreparedStatement ps = conTransaccion.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            return ps.executeUpdate() > 0;
        }
    }
}
