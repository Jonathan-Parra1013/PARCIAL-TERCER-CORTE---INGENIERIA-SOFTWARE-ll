package LogicaNegocio.Descuentos;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DescuentoPorCupon implements ReglaDescuento {
    private final Map<String, BigDecimal> cupones;

    public DescuentoPorCupon(Map<String, BigDecimal> cupones) {
        this.cupones = new HashMap<>(cupones);
    }

    @Override
    public String nombre() { return "Cupón"; }

    @Override
    public BigDecimal porcentaje(Compra compra) {
        if (compra.getCupon() == null) return BigDecimal.ZERO;
        return cupones.getOrDefault(compra.getCupon().trim().toUpperCase(), BigDecimal.ZERO);
    }
}
