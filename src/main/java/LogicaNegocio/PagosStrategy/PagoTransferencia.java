package LogicaNegocio.PagosStrategy;

import LogicaNegocio.PagosStrategy.FormatoDinero;
import LogicaNegocio.PagosStrategy.DatosPago;
import LogicaNegocio.PagosStrategy.MedioPago;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author jonap
 */

public class PagoTransferencia implements ProcesadorPago {

   
    public static final List<String> BANCOS_DISPONIBLES =
            List.of("Bancolombia", "Davivienda", "Banco de Bogotá", "Nequi");

    @Override
    public MedioPago getMedio() {
        return MedioPago.TRANSFERENCIA;
    }

    @Override
    public ResultadoPago procesar(BigDecimal monto, DatosPago datos) {
        String banco = datos.getBanco();
        if (banco == null || !BANCOS_DISPONIBLES.contains(banco)) {
            return ResultadoPago.rechazado("Seleccione un banco válido para la transferencia.");
        }
        return ResultadoPago.aprobado(FormatoDinero.referencia("TRF"),
                "Transferencia de " + FormatoDinero.pesos(monto) + " registrada desde " + banco + ".");
    }
}
