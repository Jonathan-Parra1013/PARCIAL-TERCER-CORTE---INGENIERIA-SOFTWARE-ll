package LogicaNegocio.Pedido;

/**
 *
 * @author jonap
 */

import LogicaNegocio.MedioPago;
import LogicaNegocio.Pedido.EstadoPedido;
import LogicaNegocio.Pedido.DetallePedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Pedido {
    private final int id; // 0 mientras no se haya guardado; el id es el "número de pedido"
    private final int idCliente;
    private final LocalDateTime fecha;
    private final List<DetallePedido> detalles; // puede venir vacía en listados del historial
    private final BigDecimal subtotal;
    private final BigDecimal total;
    private final MedioPago medioPago;
    private final EstadoPedido estado;
    private final String direccionEnvio;

    // para reconstruir desde la base de datos (el DAO)
    public Pedido(int id, int idCliente, LocalDateTime fecha, List<DetallePedido> detalles,
                  BigDecimal subtotal, BigDecimal total, MedioPago medioPago,
                  EstadoPedido estado, String direccionEnvio) {
        this.id = id;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.detalles = Collections.unmodifiableList(new ArrayList<>(detalles));
        this.subtotal = subtotal;
        this.total = total;
        this.medioPago = medioPago;
        this.estado = estado;
        this.direccionEnvio = direccionEnvio;
    }

 
    public static Pedido nuevo(int idCliente, List<DetallePedido> detalles,
                               BigDecimal subtotal, BigDecimal total,
                               MedioPago medioPago, String direccionEnvio) {
        if (idCliente <= 0) {
            throw new PedidoException("El pedido debe pertenecer a un cliente.");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new PedidoException("El pedido no tiene productos.");
        }
        if (medioPago == null) {
            throw new PedidoException("Seleccione un método de pago.");
        }
        if (direccionEnvio == null || direccionEnvio.isBlank()) {
            throw new PedidoException("La dirección de envío es obligatoria.");
        }
        if (direccionEnvio.trim().length() > 255) {
            throw new PedidoException("La dirección de envío es demasiado larga (máximo 255 caracteres).");
        }
        BigDecimal sumaDetalles = BigDecimal.ZERO;
        for (DetallePedido d : detalles) {
            sumaDetalles = sumaDetalles.add(d.getSubtotal());
        }
        if (subtotal == null || sumaDetalles.compareTo(subtotal) != 0) {
            throw new PedidoException("El subtotal no coincide con los productos del pedido.");
        }
        if (total == null || total.signum() < 0 || total.compareTo(subtotal) > 0) {
            throw new PedidoException("El total no es válido: no puede ser negativo ni superar el subtotal.");
        }
        return new Pedido(0, idCliente, LocalDateTime.now(), detalles, subtotal, total,
                medioPago, medioPago.getEstadoInicial(), direccionEnvio.trim());
    }

    public int getId() { return id; }
    public int getIdCliente() { return idCliente; }
    public LocalDateTime getFecha() { return fecha; }
    public List<DetallePedido> getDetalles() { return detalles; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getTotal() { return total; }
    public MedioPago getMedioPago() { return medioPago; }
    public EstadoPedido getEstado() { return estado; }
    public String getDireccionEnvio() { return direccionEnvio; }


    public BigDecimal getDescuento() { return subtotal.subtract(total); }
}
