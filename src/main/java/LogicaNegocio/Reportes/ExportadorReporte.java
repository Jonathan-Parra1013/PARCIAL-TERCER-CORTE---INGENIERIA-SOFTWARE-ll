package LogicaNegocio.Reportes;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Patrón Strategy: cada formato de archivo es una estrategia intercambiable.
 */
public interface ExportadorReporte {
    /** Extensión sin punto: "csv", "pdf". */
    String extension();

    void exportar(ReporteDiario reporte, Path destino) throws IOException;
}
