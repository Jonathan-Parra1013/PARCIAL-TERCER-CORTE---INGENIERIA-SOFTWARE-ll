package LogicaNegocio.Descuentos;
import java.math.BigDecimal;

public class DescuentoPorVolumen implements ReglaDescuento {
    @Override
    public String nombre() { return "Descuento por volumen"; }

    @Override
    public BigDecimal porcentaje(Compra compra) {
        if (compra.getCantidadItems() >= 10) return new BigDecimal("10");
        if (compra.getCantidadItems() >= 5) return new BigDecimal("5");
        return BigDecimal.ZERO;
    }
}
