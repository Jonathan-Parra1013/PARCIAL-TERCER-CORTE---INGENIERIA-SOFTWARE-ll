package LogicaNegocio;



/**
 *
 * @author jonap
 */


import LogicaNegocio.Descuentos.CalculadoraDescuentos;
import LogicaNegocio.Descuentos.Compra;
import LogicaNegocio.Descuentos.ResultadoDescuento;
import LogicaNegocio.Producto;
import java.util.List;


public class CarritoService {
    private final Carrito carrito = new Carrito();
    private final CalculadoraDescuentos calculadora;

    public CarritoService(CalculadoraDescuentos calculadora) {
        this.calculadora = calculadora;
    }

    public void agregar(Producto producto, int cantidad) { carrito.agregar(producto, cantidad); }

    public void cambiarCantidad(int idProducto, int cantidad) { carrito.cambiarCantidad(idProducto, cantidad); }

    public void eliminar(int idProducto) { carrito.eliminar(idProducto); }

    public void vaciar() { carrito.vaciar(); }

    public boolean estaVacio() { return carrito.estaVacio(); }

    public List<ItemCarrito> getItems() { return carrito.getItems(); }

  
    public ResultadoDescuento resumen(int comprasPrevias, String cupon) {
        if (carrito.estaVacio()) {
            throw new CarritoException("El carrito está vacío.");
        }
        return calculadora.calcular(
                new Compra(carrito.getSubtotal(), carrito.getCantidadItems(), comprasPrevias, cupon));
    }
}
