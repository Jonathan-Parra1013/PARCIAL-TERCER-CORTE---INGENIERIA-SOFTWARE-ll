package LogicaNegocio.Carrito;
/**
 *
 * @author jonap
 */

//un manejo de errores :D
public class CarritoException extends RuntimeException {
    public CarritoException(String mensaje) {
        super(mensaje);
    }
}
