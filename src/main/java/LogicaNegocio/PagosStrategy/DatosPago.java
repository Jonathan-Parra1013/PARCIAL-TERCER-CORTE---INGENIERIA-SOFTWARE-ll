package LogicaNegocio.PagosStrategy;

/**
 *
 * @author jonap
 */


public class DatosPago {
    private final String numeroTarjeta;
    private final String vencimiento; // formato MM/AA
    private final String cvv;
    private final String banco;

    private DatosPago(String numeroTarjeta, String vencimiento, String cvv, String banco) {
        this.numeroTarjeta = numeroTarjeta;
        this.vencimiento = vencimiento;
        this.cvv = cvv;
        this.banco = banco;
    }

    public static DatosPago tarjeta(String numeroTarjeta, String vencimiento, String cvv) {
        return new DatosPago(numeroTarjeta, vencimiento, cvv, null);
    }

    public static DatosPago transferencia(String banco) {
        return new DatosPago(null, null, null, banco);
    }

// al ser contra enttrega no necesita datos de pago :b
    public static DatosPago ninguno() {
        return new DatosPago(null, null, null, null);
    }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public String getVencimiento() { return vencimiento; }
    public String getCvv() { return cvv; }
    public String getBanco() { return banco; }

    // para que un System.out.println accidental no muestre datos de la tarjeta :)
    @Override
    public String toString() {
        return "DatosPago[ocultos]";
    }
}
