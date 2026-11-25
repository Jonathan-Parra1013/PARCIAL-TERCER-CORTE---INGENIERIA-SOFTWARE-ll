package LogicaNegocio.Pedido;

import LogicaNegocio.Carrito.CarritoService;
import LogicaNegocio.Carrito.ItemCarrito;
import LogicaNegocio.Descuentos.ResultadoDescuento;
import LogicaNegocio.PagosStrategy.DatosPago;
import LogicaNegocio.PagosStrategy.MedioPago;
import LogicaNegocio.PagosStrategy.PasarelaPagos;
import LogicaNegocio.PagosStrategy.ResultadoPago;
import LogicaNegocio.Usuario;
import java.util.ArrayList;
import java.util.List;


public class PedidoService {
    private final CarritoService carrito;
    private final PasarelaPagos pasarela;
    private final PedidoRepositorio repositorio;

    public PedidoService(CarritoService carrito, PasarelaPagos pasarela, PedidoRepositorio repositorio) {
        this.carrito = carrito;
        this.pasarela = pasarela;
        this.repositorio = repositorio;
    }


    public ResultadoDescuento resumenCompra(Usuario cliente, String cupon) {
        validarCliente(cliente);
        // las compras previas salen de la base de datos: alimentan el descuento de cliente frecuente
        int comprasPrevias = repositorio.contarPorCliente(cliente.getId());
        return carrito.resumen(comprasPrevias, cupon);
    }


    public ConfirmacionCompra confirmarCompra(Usuario cliente, MedioPago medio, DatosPago datos,
                                              String direccionEnvio, String cupon) {
        ResultadoDescuento descuento = resumenCompra(cliente, cupon); // valida cliente y carrito vacío

        List<DetallePedido> detalles = new ArrayList<>();
        for (ItemCarrito item : carrito.getItems()) {
            detalles.add(new DetallePedido(
                    item.getProducto().getId(),
                    item.getProducto().getNombre(),
                    item.getCantidad(),
                    item.getPrecioUnitario()));
        }
        Pedido pedido = Pedido.nuevo(cliente.getId(), detalles, descuento.getSubtotal(),
                descuento.getTotal(), medio, direccionEnvio);

        ResultadoPago pago = pasarela.procesar(medio, descuento.getTotal(), datos);
        if (!pago.isAprobado()) {
            throw new PedidoException(pago.getMensaje());
        }

        // Si esto falla (por ejemplo, otro cliente se llevó el stock), el pago simulado ya se aprobó
        // pero no queda registrado. Con una pasarela real habría que reversarlo.
        int numeroPedido = repositorio.crear(pedido);

        carrito.vaciar();
        return new ConfirmacionCompra(numeroPedido, pedido, pago, descuento);
    }


    public List<Pedido> historial(Usuario cliente) {
        validarCliente(cliente);
        return repositorio.listarPorCliente(cliente.getId());
    }

    public Pedido detalle(Usuario cliente, int idPedido) {
        validarCliente(cliente);
        Pedido pedido = repositorio.buscarPorId(idPedido);
        // mismo mensaje si no existe o es de otro cliente: no se revela que existe
        if (pedido == null || pedido.getIdCliente() != cliente.getId()) {
            throw new PedidoException("No se encontró el pedido.");
        }
        return pedido;
    }

    private void validarCliente(Usuario cliente) {
        if (cliente == null) {
            throw new PedidoException("Debes iniciar sesión para comprar.");
        }
        if (!"CLIENTE".equalsIgnoreCase(cliente.getRol())) {
            throw new PedidoException("Solo los clientes pueden realizar compras.");
        }
    }
}
