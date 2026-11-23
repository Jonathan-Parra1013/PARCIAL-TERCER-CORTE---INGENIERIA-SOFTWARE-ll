package LogicaNegocio.Pedido;

import java.math.BigDecimal;

/**
 *
 * @author jonap
 */
public class DetallePedido {
    private final int idProducto;
    private final String nombreProducto;
    private final int cantidad;
    private final BigDecimal precioUnitario; // columna precio_unitario_aplicado

    public DetallePedido(int idProducto, String nombreProducto, int cantidad, BigDecimal precioUnitario) {
        if (cantidad <= 0) {
            throw new PedidoException("La cantidad debe ser mayor a cero.");
        }
        if (precioUnitario == null || precioUnitario.signum() <= 0) {
            throw new PedidoException("El precio unitario debe ser mayor a cero.");
        }
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
