package Repositorios;

import LogicaNegocio.Producto.Producto;
import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepositorio {

    List<Producto> filtrar(String categoria, BigDecimal precioMin, BigDecimal precioMax, String texto);

    List<Producto> listarPorVendedor(int idVendedor);

    Producto buscarPorId(int idProducto);

    boolean crear(Producto producto);

    boolean actualizar(Producto producto);

    boolean desactivar(int idProducto, int idVendedor);

    List<String> listarCategorias();
}
