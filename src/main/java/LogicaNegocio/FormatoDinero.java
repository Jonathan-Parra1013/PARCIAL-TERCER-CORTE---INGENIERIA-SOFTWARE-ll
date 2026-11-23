package LogicaNegocio.Pagos;

import java.math.BigDecimal;
import java.util.Locale;

/** Da formato de pesos colombianos a los montos de los mensajes: 405000 -> $405.000 */
final class FormatoDinero {
    private static final Locale CO = Locale.forLanguageTag("es-CO");

    private FormatoDinero() { }

    static String pesos(BigDecimal monto) {
        return String.format(CO, "$%,.0f", monto);
    }

    /** Genera un código corto para las referencias, por ejemplo TRJ-9F3A21BC. */
    static String referencia(String prefijo) {
        return prefijo + "-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
