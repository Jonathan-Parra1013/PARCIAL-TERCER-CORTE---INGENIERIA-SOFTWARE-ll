package LogicaNegocio.Reportes;

import java.math.BigDecimal;

public class ProductoVendido {
    private final String nombre;
    private final long unidades;
    private final BigDecimal ingresos;

    public ProductoVendido(String nombre, long unidades, BigDecimal ingresos) {
        this.nombre = nombre;
        this.unidades = unidades;
        this.ingresos = ingresos == null ? BigDecimal.ZERO : ingresos;
    }

    public String getNombre() { return nombre; }
    public long getUnidades() { return unidades; }
    public BigDecimal getIngresos() { return ingresos; }
}