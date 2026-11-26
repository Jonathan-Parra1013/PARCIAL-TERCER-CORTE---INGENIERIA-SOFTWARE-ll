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


public class ReporteDiario {
    private final LocalDate fecha;
    private final String vendedor; // null si el reporte no es de un vendedor concreto
    private final int numeroVentas;
    private final BigDecimal ingresosTotales;
    private final List<ProductoVendido> productosVendidos; // ordenados de mayor a menor
    private final List<LineaVenta> lineas; // detalle por pedido; vacío si no se cargó

    public ReporteDiario(LocalDate fecha, int numeroVentas, BigDecimal ingresosTotales,
                         List<ProductoVendido> productosVendidos) {
        this(fecha, null, numeroVentas, ingresosTotales, productosVendidos, null);
    }

    public ReporteDiario(LocalDate fecha, String vendedor, int numeroVentas, BigDecimal ingresosTotales,
                         List<ProductoVendido> productosVendidos, List<LineaVenta> lineas) {
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.numeroVentas = Math.max(numeroVentas, 0);
        this.ingresosTotales = (ingresosTotales == null ? BigDecimal.ZERO : ingresosTotales)
                .setScale(2, RoundingMode.HALF_UP);
        this.productosVendidos = productosVendidos == null
                ? Collections.<ProductoVendido>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(productosVendidos));
        this.lineas = lineas == null
                ? Collections.<LineaVenta>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(lineas));
    }

    public LocalDate getFecha() { return fecha; }
    public String getVendedor() { return vendedor; }
    public int getNumeroVentas() { return numeroVentas; }
    public BigDecimal getIngresosTotales() { return ingresosTotales; }


    public List<ProductoVendido> getProductosVendidos() { return productosVendidos; }

    /** Detalle por pedido (una línea por producto vendido); vacío si no hay ventas. */
    public List<LineaVenta> getLineas() { return lineas; }


    public List<ProductoVendido> getTopProductos(int n) {
        return productosVendidos.subList(0, Math.min(Math.max(n, 0), productosVendidos.size()));
    }

    public boolean sinVentas() { return numeroVentas == 0; }


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
