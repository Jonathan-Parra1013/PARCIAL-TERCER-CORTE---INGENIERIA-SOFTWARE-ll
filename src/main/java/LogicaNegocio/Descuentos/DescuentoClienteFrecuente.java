package LogicaNegocio.Descuentos;
import java.math.BigDecimal;

public class DescuentoClienteFrecuente implements ReglaDescuento {
    @Override
    public String nombre() { return "Descuento cliente frecuente"; }

    @Override
    public BigDecimal porcentaje(Compra compra) {
        if (compra.getComprasPrevias() >= 10) return new BigDecimal("8");
        if (compra.getComprasPrevias() >= 3) return new BigDecimal("3");
        return BigDecimal.ZERO;
    }
}
