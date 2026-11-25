package LogicaNegocio.Reportes;

import LogicaNegocio.PagosStrategy.MedioPago;
import LogicaNegocio.Pedido.EstadoPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LineaVenta {
    private final int idPedido;
    private final LocalDateTime fecha;
    private final String cliente;
    private final MedioPago medioPago;
    private final EstadoPedido estado;
    private final String producto;
    private final int cantidad;
    private final BigDecimal precioUnitario;
    private final BigDecimal subtotal;

    public LineaVenta(int idPedido, LocalDateTime fecha, String cliente, MedioPago medioPago,
                      EstadoPedido estado, String producto, int cantidad,
                      BigDecimal precioUnitario, BigDecimal subtotal) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.cliente = cliente;
        this.medioPago = medioPago;
        this.estado = estado;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public int getIdPedido() { return idPedido; }
    public LocalDateTime getFecha() { return fecha; }
    public String getCliente() { return cliente; }
    public MedioPago getMedioPago() { return medioPago; }
    public EstadoPedido getEstado() { return estado; }
    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}
