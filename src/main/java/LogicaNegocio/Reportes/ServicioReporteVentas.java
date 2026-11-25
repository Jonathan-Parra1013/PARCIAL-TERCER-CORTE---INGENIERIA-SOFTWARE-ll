package LogicaNegocio.Reportes;


import BaseDatos.VentaDAO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ServicioReporteVentas {

    private static final ZoneId ZONA = ZoneId.of("America/Bogota");

    private final VentaDAO ventaDAO;

    public ServicioReporteVentas() {
        this(new VentaDAO());
    }

    public ServicioReporteVentas(VentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

    /** Reporte de hoy: ingresos, número de ventas y productos vendidos. */
    public ReporteDiario generarReporteDelDia() {
        return generarReporte(LocalDate.now(ZONA));
    }

    public ReporteDiario generarReporte(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        LocalDateTime desde = fecha.atStartOfDay();
        LocalDateTime hasta = fecha.plusDays(1).atStartOfDay();

        // Sin ventas: 0 pedidos, $0.00 y lista vacía (el DAO nunca devuelve null)
        return new ReporteDiario(
                fecha,
                ventaDAO.contarVentas(desde, hasta),
                ventaDAO.sumarIngresos(desde, hasta),
                ventaDAO.productosVendidos(desde, hasta));
    }

    /** Escribe el reporte en el archivo indicado con el formato (Strategy) elegido. */
    public Path exportar(ReporteDiario reporte, ExportadorReporte exportador, Path archivo) throws IOException {
        if (reporte == null || exportador == null || archivo == null) {
            throw new IllegalArgumentException("Reporte, exportador y archivo son obligatorios");
        }
        if (archivo.getParent() != null) {
            Files.createDirectories(archivo.getParent());
        }
        exportador.exportar(reporte, archivo);
        return archivo;
    }

    /** Nombre por defecto, útil para JFileChooser: reporte_ventas_2026-11-20.pdf */
    public String nombreSugerido(ReporteDiario reporte, ExportadorReporte exportador) {
        return "reporte_ventas_" + reporte.getFecha() + "." + exportador.extension();
    }
}
