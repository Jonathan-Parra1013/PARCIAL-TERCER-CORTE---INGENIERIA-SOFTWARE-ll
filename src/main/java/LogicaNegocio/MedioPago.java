package LogicaNegocio;

/**
 *
 * @author jonap
 */


public enum MedioPago {
    TARJETA("Tarjeta", EstadoPedido.PAGADO),
    TRANSFERENCIA("Transferencia", EstadoPedido.PAGADO),
    CONTRA_ENTREGA("Contra entrega", EstadoPedido.PENDIENTE);

    private final String etiqueta;
    private final EstadoPedido estadoInicial;

    MedioPago(String etiqueta, EstadoPedido estadoInicial) {
        this.etiqueta = etiqueta;
        this.estadoInicial = estadoInicial;
    }

    public String getEtiqueta() { return etiqueta; }

    public EstadoPedido getEstadoInicial() { return estadoInicial; }
}
