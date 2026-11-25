package LogicaNegocio.Pedido;

import java.util.List;


public interface PedidoRepositorio {


    int crear(Pedido pedido);


    List<Pedido> listarPorCliente(int idCliente);


    Pedido buscarPorId(int idPedido);


    int contarPorCliente(int idCliente);
}
