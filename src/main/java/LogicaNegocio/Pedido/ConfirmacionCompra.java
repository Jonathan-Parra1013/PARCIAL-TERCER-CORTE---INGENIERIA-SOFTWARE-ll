package LogicaNegocio.Pedido;

import LogicaNegocio.Descuentos.ResultadoDescuento;
import LogicaNegocio.PagosStrategy.ResultadoPago;


public class ConfirmacionCompra {
    private final int numeroPedido;
    private final Pedido pedido;
    private final ResultadoPago pago;
    private final ResultadoDescuento descuento;

    public ConfirmacionCompra(int numeroPedido, Pedido pedido, ResultadoPago pago, ResultadoDescuento descuento) {
        this.numeroPedido = numeroPedido;
        this.pedido = pedido;
        this.pago = pago;
        this.descuento = descuento;
    }

    public int getNumeroPedido() { return numeroPedido; }

    public Pedido getPedido() { return pedido; }
    public ResultadoPago getPago() { return pago; }

    public ResultadoDescuento getDescuento() { return descuento; }
}
