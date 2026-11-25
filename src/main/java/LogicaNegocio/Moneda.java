package LogicaNegocio;

import java.math.BigDecimal;
import java.util.Locale;


public final class Moneda {
    private static final Locale CO = Locale.forLanguageTag("es-CO");

    private Moneda() { }

    public static String pesos(BigDecimal monto) {
        return String.format(CO, "$%,.0f", monto);
    }
}
