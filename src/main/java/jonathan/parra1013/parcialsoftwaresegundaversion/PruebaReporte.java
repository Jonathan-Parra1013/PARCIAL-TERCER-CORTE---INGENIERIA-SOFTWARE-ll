package jonathan.parra1013.parcialsoftwaresegundaversion;

import LogicaNegocio.Reportes.ExportadorCSV;
import LogicaNegocio.Reportes.ExportadorPDF;
import LogicaNegocio.Reportes.ExportadorReporte;
import LogicaNegocio.Reportes.ProductoVendido;
import LogicaNegocio.Reportes.ReporteDiario;
import LogicaNegocio.Reportes.ServicioReporteVentas;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class PruebaReporte {

    // Usa la base de datos real
    public static void pruebaConBD() throws IOException {
        ServicioReporteVentas servicio = new ServicioReporteVentas();
        ReporteDiario reporte = servicio.generarReporteDelDia();
        mostrar(reporte);
        exportarAmbos(servicio, reporte);
    }

    // No necesita base de datos: comprueba el caso "sin ventas"
    public static void pruebaSinVentas() throws IOException {
        ServicioReporteVentas servicio = new ServicioReporteVentas();
        ReporteDiario vacio = new ReporteDiario(LocalDate.now(), 0, null, null);
        mostrar(vacio);
        exportarAmbos(servicio, vacio);
    }

    private static void mostrar(ReporteDiario r) {
        System.out.println("== Reporte del " + r.getFecha() + " ==");
        System.out.println("Ingresos totales: " + ReporteDiario.dinero(r.getIngresosTotales()));
        System.out.println("Número de ventas: " + r.getNumeroVentas());
        System.out.println("Resumen: " + r.resumen());
        if (r.sinVentas()) {
            System.out.println("Sin ventas registradas hoy.");
        } else {
            System.out.println("Más vendidos:");
            for (ProductoVendido p : r.getTopProductos(5)) {
                System.out.println("  - " + p.getNombre() + " (" + p.getUnidades() + " unidades)");
            }
        }
        System.out.println();
    }

    private static void exportarAmbos(ServicioReporteVentas servicio, ReporteDiario reporte) throws IOException {
        Path carpeta = Paths.get(System.getProperty("user.home"), "reportes_ventas");
        for (ExportadorReporte exportador : List.of(new ExportadorCSV(), new ExportadorPDF())) {
            Path archivo = carpeta.resolve(servicio.nombreSugerido(reporte, exportador));
            servicio.exportar(reporte, exportador, archivo);
            System.out.println("Archivo generado: " + archivo);
        }
    }
}
