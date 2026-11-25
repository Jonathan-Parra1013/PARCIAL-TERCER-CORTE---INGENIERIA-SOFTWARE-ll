package LogicaNegocio.Reportes;

import java.time.LocalDate;
import java.util.List;

public interface VentasRepositorio {

    List<LineaVenta> listarVentasDelDia(int idVendedor, LocalDate dia);
}
