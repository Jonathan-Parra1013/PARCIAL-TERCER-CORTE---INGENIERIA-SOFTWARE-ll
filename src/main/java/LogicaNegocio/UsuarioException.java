package LogicaNegocio;


public class UsuarioException extends RuntimeException {
    private final String campo;

    public UsuarioException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() { return campo; }
}
