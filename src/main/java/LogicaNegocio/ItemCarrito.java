package LogicaNegocio;

import LogicaNegocio.Producto;
import java.math.BigDecimal;

//  un producto y cuántas unidades lleva el cliente :b
public class ItemCarrito {
    private final Producto producto;
    private final BigDecimal precioUnitario; // precio al momento de agregarlo
    private int cantidad;

    ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.precioUnitario = producto.getPrecio();
        this.cantidad = cantidad;
    }

    public Producto getProducto() { return producto; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }

    void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
