package LogicaNegocio.PagosStrategy;

import LogicaNegocio.PagosStrategy.FormatoDinero;
import LogicaNegocio.PagosStrategy.DatosPago;
import LogicaNegocio.PagosStrategy.MedioPago;
import LogicaNegocio.ResultadoPago;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author jonap
 */


public class PagoTarjeta implements ProcesadorPago {

    private static final DateTimeFormatter FORMATO_VENCIMIENTO = DateTimeFormatter.ofPattern("MM/yy");

    @Override
    public MedioPago getMedio() {
        return MedioPago.TARJETA;
    }

    @Override
    public ResultadoPago procesar(BigDecimal monto, DatosPago datos) {
        String numero = limpiar(datos.getNumeroTarjeta());
        if (!numero.matches("\\d{13,19}") || !pasaLuhn(numero)) {
            return ResultadoPago.rechazado("El número de tarjeta no es válido.");
        }

        YearMonth vencimiento;
        try {
            vencimiento = YearMonth.parse(datos.getVencimiento() == null ? "" : datos.getVencimiento().trim(),
                    FORMATO_VENCIMIENTO);
        } catch (DateTimeParseException e) {
            return ResultadoPago.rechazado("La fecha de vencimiento no es válida (use MM/AA).");
        }
        if (vencimiento.isBefore(YearMonth.now())) {
            return ResultadoPago.rechazado("La tarjeta está vencida.");
        }

        String cvv = datos.getCvv() == null ? "" : datos.getCvv().trim();
        if (!cvv.matches("\\d{3,4}")) {
            return ResultadoPago.rechazado("El CVV no es válido.");
        }

        if (numero.endsWith("0002")) {
            return ResultadoPago.rechazado("Pago rechazado por el banco: fondos insuficientes.");
        }

        String ultimos4 = numero.substring(numero.length() - 4);
        return ResultadoPago.aprobado(FormatoDinero.referencia("TRJ"),
                "Pago de " + FormatoDinero.pesos(monto) + " aprobado con la tarjeta terminada en " + ultimos4 + ".");
    }

    // quita espacios y guiones: "4242 4242-4242 4242" -> "4242424242424242"
    private static String limpiar(String numero) {
        return numero == null ? "" : numero.replaceAll("[\\s-]", "");
    }

    // algoritmo de Luhn: detecta números de tarjeta mal digitados
    private static boolean pasaLuhn(String numero) {
        int suma = 0;
        boolean doblar = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = numero.charAt(i) - '0';
            if (doblar) {
                digito *= 2;
                if (digito > 9) {
                    digito -= 9;
                }
            }
            suma += digito;
            doblar = !doblar;
        }
        return suma % 10 == 0;
    }
}
