package LogicaNegocio.Descuentos;
import java.math.BigDecimal;
import java.util.List;

public class ResultadoDescuento {
    private final BigDecimal subtotal;
    private final BigDecimal porcentajeAplicado;
    private final BigDecimal montoDescuento;
    private final BigDecimal total;
    private final List<String> detalle;

    public ResultadoDescuento(BigDecimal subtotal, BigDecimal porcentajeAplicado,
                              BigDecimal montoDescuento, BigDecimal total, List<String> detalle) {
        this.subtotal = subtotal;
        this.porcentajeAplicado = porcentajeAplicado;
        this.montoDescuento = montoDescuento;
        this.total = total;
        this.detalle = detalle;
    }

    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getPorcentajeAplicado() { return porcentajeAplicado; }
    public BigDecimal getMontoDescuento() { return montoDescuento; }
    public BigDecimal getTotal() { return total; }
    public List<String> getDetalle() { return detalle; }
}
