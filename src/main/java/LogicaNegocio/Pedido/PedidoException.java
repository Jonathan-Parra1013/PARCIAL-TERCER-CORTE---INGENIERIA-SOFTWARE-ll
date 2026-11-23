package LogicaNegocio.Pedido;

// un manejo de error por si acaso :b
public class PedidoException extends RuntimeException {
    public PedidoException(String mensaje) {
        super(mensaje);
    }
}
