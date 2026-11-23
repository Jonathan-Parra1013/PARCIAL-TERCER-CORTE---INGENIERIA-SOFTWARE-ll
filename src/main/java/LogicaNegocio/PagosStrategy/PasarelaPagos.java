package LogicaNegocio.PagosStrategy;

import LogicaNegocio.PagosStrategy.*;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jonap
 */

public class PasarelaPagos {
    private final Map<MedioPago, ProcesadorPago> procesadores = new EnumMap<>(MedioPago.class);

    public PasarelaPagos(List<ProcesadorPago> estrategias) {
        for (ProcesadorPago p : estrategias) {
            if (procesadores.put(p.getMedio(), p) != null) {
                throw new IllegalArgumentException("Hay dos procesadores para el medio " + p.getMedio());
            }
        }
    }


    public List<MedioPago> getMediosDisponibles() {
        return List.copyOf(procesadores.keySet());
    }


    public ResultadoPago procesar(MedioPago medio, BigDecimal monto, DatosPago datos) {
        if (medio == null) {
            return ResultadoPago.rechazado("Seleccione un método de pago.");
        }
        ProcesadorPago procesador = procesadores.get(medio);
        if (procesador == null) {
            return ResultadoPago.rechazado("El método de pago " + medio.getEtiqueta() + " no está disponible.");
        }
        if (monto == null || monto.signum() <= 0) {
            return ResultadoPago.rechazado("El monto a pagar debe ser mayor a cero.");
        }
        return procesador.procesar(monto, datos == null ? DatosPago.ninguno() : datos);
    }
}
