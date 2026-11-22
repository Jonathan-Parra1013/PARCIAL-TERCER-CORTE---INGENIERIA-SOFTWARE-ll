package LogicaNegocio;

import LogicaNegocio.Producto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jonap
 */
public class Carrito {
    // LinkedHashMap: conserva el orden en que se agregaron los productos
    private final Map<Integer, ItemCarrito> items = new LinkedHashMap<>();

    /** RF-11.1: agrega unidades de un producto. Si ya estaba, suma la cantidad. */
    public void agregar(Producto producto, int cantidad) {
        if (producto == null) {
            throw new CarritoException("El producto no existe.");
        }
        if (cantidad <= 0) {
            throw new CarritoException("La cantidad debe ser mayor a cero.");
        }
        ItemCarrito existente = items.get(producto.getId());
        int total = cantidad + (existente == null ? 0 : existente.getCantidad());
        if (total > producto.getStock()) {
            throw new CarritoException("Stock insuficiente de \"" + producto.getNombre()
                    + "\". Disponible: " + producto.getStock() + ".");
        }
        if (existente == null) {
            items.put(producto.getId(), new ItemCarrito(producto, cantidad));
        } else {
            existente.setCantidad(total);
        }
    }

    /** Cambia la cantidad de un producto ya agregado. Con 0 lo elimina. */
    public void cambiarCantidad(int idProducto, int nuevaCantidad) {
        ItemCarrito item = items.get(idProducto);
        if (item == null) {
            throw new CarritoException("Ese producto no está en el carrito.");
        }
        if (nuevaCantidad < 0) {
            throw new CarritoException("La cantidad no puede ser negativa.");
        }
        if (nuevaCantidad == 0) {
            items.remove(idProducto);
            return;
        }
        if (nuevaCantidad > item.getProducto().getStock()) {
            throw new CarritoException("Stock insuficiente. Disponible: "
                    + item.getProducto().getStock() + ".");
        }
        item.setCantidad(nuevaCantidad);
    }

    /** RF-11.2: quita un producto completo del carrito. */
    public void eliminar(int idProducto) {
        if (items.remove(idProducto) == null) {
            throw new CarritoException("Ese producto no está en el carrito.");
        }
    }

    public void vaciar() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public List<ItemCarrito> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    /** Total de unidades (no de líneas). Es el dato que usa DescuentoPorVolumen. */
    public int getCantidadItems() {
        int total = 0;
        for (ItemCarrito item : items.values()) {
            total += item.getCantidad();
        }
        return total;
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (ItemCarrito item : items.values()) {
            subtotal = subtotal.add(item.getSubtotal());
        }
        return subtotal;
    }
}
