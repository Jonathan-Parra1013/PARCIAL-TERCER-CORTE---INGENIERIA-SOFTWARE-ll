package LogicaNegocio.Reportes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Resultado del reporte de un día. Es "a prueba de vacío": si no hay ventas,
 * los valores nulos se convierten en 0.00, 0 pedidos y lista vacía.
 */
public class ReporteDiario {
    private final LocalDate fecha;
    private final int numeroVentas;
    private final BigDecimal ingresosTotales;
    private final List<ProductoVendido> productosVendidos; // ordenados de mayor a menor

    public ReporteDiario(LocalDate fecha, int numeroVentas, BigDecimal ingresosTotales,
                         List<ProductoVendido> productosVendidos) {
        this.fecha = fecha;
        this.numeroVentas = Math.max(numeroVentas, 0);
        this.ingresosTotales = (ingresosTotales == null ? BigDecimal.ZERO : ingresosTotales)
                .setScale(2, RoundingMode.HALF_UP);
        this.productosVendidos = productosVendidos == null
                ? Collections.<ProductoVendido>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(productosVendidos));
    }

    public LocalDate getFecha() { return fecha; }
    public int getNumeroVentas() { return numeroVentas; }
    public BigDecimal getIngresosTotales() { return ingresosTotales; }

    /** Todos los productos vendidos en el día (para el desglose del archivo). */
    public List<ProductoVendido> getProductosVendidos() { return productosVendidos; }

    /** Los n productos más vendidos (para mostrar en pantalla). */
    public List<ProductoVendido> getTopProductos(int n) {
        return productosVendidos.subList(0, Math.min(Math.max(n, 0), productosVendidos.size()));
    }

    public boolean sinVentas() { return numeroVentas == 0; }

    /** Ejemplo: "$0.00 / 0 pedidos" */
    public String resumen() {
        return dinero(ingresosTotales) + " / " + numeroVentas
                + (numeroVentas == 1 ? " pedido" : " pedidos");
    }

    public static String dinero(BigDecimal valor) {
        BigDecimal v = valor == null ? BigDecimal.ZERO : valor;
        DecimalFormat df = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return "$" + df.format(v);
    }
}
