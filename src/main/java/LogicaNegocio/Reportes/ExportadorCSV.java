package LogicaNegocio.Reportes;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class ExportadorCSV implements ExportadorReporte {

    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public String extension() { return "csv"; }

    @Override
    public void exportar(ReporteDiario reporte, Path destino) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append('\uFEFF'); // BOM para que Excel muestre bien las tildes

        fila(sb, "Reporte de ventas", String.valueOf(reporte.getFecha()));
        if (reporte.getVendedor() != null) {
            fila(sb, "Vendedor", seguro(reporte.getVendedor()));
        }
        fila(sb, "Ingresos totales", plano(reporte.getIngresosTotales()));
        fila(sb, "Número de ventas", String.valueOf(reporte.getNumeroVentas()));
        if (reporte.getVendedor() != null) {
            fila(sb, "Nota", "Valores antes de los descuentos aplicados al pedido");
        }
        sb.append("\r\n");

        fila(sb, "Producto", "Unidades vendidas", "Ingresos");
        if (reporte.getProductosVendidos().isEmpty()) {
            fila(sb, "Sin ventas registradas en esta fecha");
        } else {
            for (ProductoVendido p : reporte.getProductosVendidos()) {
                fila(sb, seguro(p.getNombre()), String.valueOf(p.getUnidades()), plano(p.getIngresos()));
            }
        }

        if (!reporte.getLineas().isEmpty()) {
            sb.append("\r\n");
            fila(sb, "Detalle por pedido");
            fila(sb, "Pedido", "Fecha", "Cliente", "Medio de pago", "Estado",
                    "Producto", "Cantidad", "Precio unitario", "Subtotal");
            for (LineaVenta v : reporte.getLineas()) {
                fila(sb,
                        String.valueOf(v.getIdPedido()),
                        v.getFecha().format(FECHA_HORA),
                        seguro(v.getCliente()),
                        v.getMedioPago().getEtiqueta(),
                        v.getEstado().getEtiqueta(),
                        seguro(v.getProducto()),
                        String.valueOf(v.getCantidad()),
                        plano(v.getPrecioUnitario()),
                        plano(v.getSubtotal()));
            }
        }
        Files.write(destino, sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static void fila(StringBuilder sb, String... celdas) {
        for (int i = 0; i < celdas.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(celda(celdas[i]));
        }
        sb.append("\r\n");
    }

    private static String plano(BigDecimal valor) {
        return valor == null ? "" : valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    // Excel ejecuta como fórmula lo que empieza con = + - @ ; los nombres vienen de usuarios,
    // así que se les antepone una comilla para que se lean como texto.
    private static String seguro(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "";
        }
        char c = valor.charAt(0);
        if (c == '=' || c == '+' || c == '-' || c == '@' || c == '\t' || c == '\r') {
            return "'" + valor;
        }
        return valor;
    }

    private static String celda(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
