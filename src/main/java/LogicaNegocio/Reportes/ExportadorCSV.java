package LogicaNegocio.Reportes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExportadorCSV implements ExportadorReporte {

    @Override
    public String extension() { return "csv"; }

    @Override
    public void exportar(ReporteDiario reporte, Path destino) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append('\uFEFF'); // BOM para que Excel muestre bien las tildes
        sb.append("Reporte de ventas,").append(reporte.getFecha()).append("\r\n");
        sb.append("Ingresos totales,").append(reporte.getIngresosTotales().toPlainString()).append("\r\n");
        sb.append("Número de ventas,").append(reporte.getNumeroVentas()).append("\r\n");
        sb.append("\r\n");
        sb.append("Producto,Unidades vendidas,Ingresos\r\n");

        if (reporte.getProductosVendidos().isEmpty()) {
            sb.append("Sin ventas registradas en esta fecha\r\n");
        } else {
            for (ProductoVendido p : reporte.getProductosVendidos()) {
                sb.append(celda(p.getNombre())).append(',')
                        .append(p.getUnidades()).append(',')
                        .append(p.getIngresos().setScale(2, java.math.RoundingMode.HALF_UP).toPlainString())
                        .append("\r\n");
            }
        }
        Files.write(destino, sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String celda(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
