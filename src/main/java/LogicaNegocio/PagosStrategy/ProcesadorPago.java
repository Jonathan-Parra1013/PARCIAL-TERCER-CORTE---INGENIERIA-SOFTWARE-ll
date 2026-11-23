package LogicaNegocio.PagosStrategy;

import LogicaNegocio.PagosStrategy.DatosPago;
import LogicaNegocio.PagosStrategy.MedioPago;
import LogicaNegocio.ResultadoPago;
import java.math.BigDecimal;
/**
 *
 * @author jonap
 */


public interface ProcesadorPago {
    MedioPago getMedio();

    ResultadoPago procesar(BigDecimal monto, DatosPago datos);
}
