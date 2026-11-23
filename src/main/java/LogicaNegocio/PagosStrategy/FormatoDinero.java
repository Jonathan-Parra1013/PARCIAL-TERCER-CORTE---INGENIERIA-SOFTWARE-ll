package LogicaNegocio.PagosStrategy;

import java.math.BigDecimal;
import java.util.Locale;

/**
 *
 * @author jonap
 */
final class FormatoDinero {
    private static final Locale CO = Locale.forLanguageTag("es-CO");

    private FormatoDinero() { }

    static String pesos(BigDecimal monto) {
        return String.format(CO, "$%,.0f", monto);
    }


    static String referencia(String prefijo) {
        return prefijo + "-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
