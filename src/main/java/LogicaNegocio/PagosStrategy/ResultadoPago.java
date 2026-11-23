package LogicaNegocio.PagosStrategy;

/**
 *
 * @author jonap
 */

public class ResultadoPago {
    private final boolean aprobado;
    private final String referencia; // solo si fue aprobado
    private final String mensaje;

    private ResultadoPago(boolean aprobado, String referencia, String mensaje) {
        this.aprobado = aprobado;
        this.referencia = referencia;
        this.mensaje = mensaje;
    }

    public static ResultadoPago aprobado(String referencia, String mensaje) {
        return new ResultadoPago(true, referencia, mensaje);
    }

    public static ResultadoPago rechazado(String mensaje) {
        return new ResultadoPago(false, null, mensaje);
    }

    public boolean isAprobado() { return aprobado; }
    public String getReferencia() { return referencia; }
    public String getMensaje() { return mensaje; }
}
