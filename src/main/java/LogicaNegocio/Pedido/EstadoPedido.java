package LogicaNegocio.Pedido;
/**
 *
 * @author jonap
 */

// Estados de un pedido los nombres coinciden con el enum de la columna pedidos.estado
public enum EstadoPedido {
    PENDIENTE("Pendiente de pago"),
    PAGADO("Pagado"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String etiqueta;

    EstadoPedido(String etiqueta) {
        this.etiqueta = etiqueta;
    }


    public String getEtiqueta() { return etiqueta; }
}
