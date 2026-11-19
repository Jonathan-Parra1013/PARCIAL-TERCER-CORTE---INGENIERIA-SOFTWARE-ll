package LogicaNegocio.Descuentos;
import java.math.BigDecimal;

public class DescuentoPorMonto implements ReglaDescuento {
    private static final BigDecimal MONTO_MINIMO = new BigDecimal("500000");

    @Override
    public String nombre() { return "Descuento por monto de compra"; }

    @Override
    public BigDecimal porcentaje(Compra compra) {
        if (compra.getSubtotal().compareTo(MONTO_MINIMO) >= 0) return new BigDecimal("7");
        return BigDecimal.ZERO;
    }
}
