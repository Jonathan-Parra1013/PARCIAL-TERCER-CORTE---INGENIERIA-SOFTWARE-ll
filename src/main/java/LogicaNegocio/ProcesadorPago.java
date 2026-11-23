package LogicaNegocio;

import LogicaNegocio.MedioPago;
import java.math.BigDecimal;
/**
 *
 * @author jonap
 */


public interface ProcesadorPago {
    MedioPago getMedio();

    ResultadoPago procesar(BigDecimal monto, DatosPago datos);
}
