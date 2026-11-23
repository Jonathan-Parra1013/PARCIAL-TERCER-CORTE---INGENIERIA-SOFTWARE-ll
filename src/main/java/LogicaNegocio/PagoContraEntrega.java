package LogicaNegocio;

import LogicaNegocio.MedioPago;
import java.math.BigDecimal;

/**
 *
 * @author jonap
 */


public class PagoContraEntrega implements ProcesadorPago {

    @Override
    public MedioPago getMedio() {
        return MedioPago.CONTRA_ENTREGA;
    }

    @Override
    public ResultadoPago procesar(BigDecimal monto, DatosPago datos) {
        return ResultadoPago.aprobado(FormatoDinero.referencia("CE"),
                "Pedido registrado. Pagarás " + FormatoDinero.pesos(monto) + " al recibirlo.");
    }
}
