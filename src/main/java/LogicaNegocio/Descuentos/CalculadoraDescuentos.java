package LogicaNegocio.Descuentos;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Contexto del patrón Strategy: recibe las reglas y las aplica sin conocer
 * los detalles de cada una. Los descuentos se suman y se limitan a un tope.
 */
public class CalculadoraDescuentos {
    private final List<ReglaDescuento> reglas;
    private final BigDecimal topePorcentaje;

    public CalculadoraDescuentos(List<ReglaDescuento> reglas, BigDecimal topePorcentaje) {
        this.reglas = new ArrayList<>(reglas);
        this.topePorcentaje = topePorcentaje;
    }

    public ResultadoDescuento calcular(Compra compra) {
        BigDecimal suma = BigDecimal.ZERO;
        List<String> detalle = new ArrayList<>();

        for (ReglaDescuento regla : reglas) {
            BigDecimal porcentaje = regla.porcentaje(compra);
            if (porcentaje.signum() > 0) {
                suma = suma.add(porcentaje);
                detalle.add(regla.nombre() + ": " + porcentaje + "%");
            }
        }

        BigDecimal aplicado = suma.min(topePorcentaje);
        if (suma.compareTo(topePorcentaje) > 0) {
            detalle.add("Se aplicó el tope máximo de " + topePorcentaje + "%");
        }

        BigDecimal monto = compra.getSubtotal()
                .multiply(aplicado)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal total = compra.getSubtotal().subtract(monto);

        return new ResultadoDescuento(compra.getSubtotal(), aplicado, monto, total, detalle);
    }
}
