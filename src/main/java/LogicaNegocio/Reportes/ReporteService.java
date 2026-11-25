package LogicaNegocio.Reportes;

import LogicaNegocio.Moneda;
import LogicaNegocio.Usuario;
import LogicaNegocio.UsuarioService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReporteService {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final String LINEA_DOBLE = "=".repeat(64);
    private static final String LINEA_SIMPLE = "-".repeat(64);

    private final VentasRepositorio repositorio;

    public ReporteService(VentasRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public String generarReporteDiario(Usuario vendedor, LocalDate dia) {
        if (vendedor == null || !UsuarioService.ROL_VENDEDOR.equalsIgnoreCase(vendedor.getRol())) {
            throw new ReporteException("Solo los vendedores pueden generar el reporte.");
        }
        List<LineaVenta> lineas = repositorio.listarVentasDelDia(vendedor.getId(), dia);

        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE DIARIO DE COMPRAS\n");
        sb.append("Vendedor: ").append(vendedor.getNombre()).append('\n');
        sb.append("Fecha:    ").append(dia.format(FECHA)).append('\n');
        sb.append(LINEA_DOBLE).append("\n\n");

        if (lineas.isEmpty()) {
            sb.append("No se registraron compras de tus productos en este día.\n");
            return sb.toString();
        }

        Set<Integer> pedidos = new HashSet<>();
        int unidades = 0;
        BigDecimal total = BigDecimal.ZERO;
        int pedidoActual = -1;

        for (LineaVenta l : lineas) {
            if (l.getIdPedido() != pedidoActual) {
                if (pedidoActual != -1) {
                    sb.append('\n');
                }
                pedidoActual = l.getIdPedido();
                sb.append(String.format("Pedido #%d  %s  Cliente: %s%n",
                        l.getIdPedido(), l.getFecha().format(HORA), l.getCliente()));
                sb.append(String.format("   Pago: %s  |  Estado: %s%n",
                        l.getMedioPago().getEtiqueta(), l.getEstado().getEtiqueta()));
            }
            sb.append(String.format("   %3d x %-26s %12s %14s%n",
                    l.getCantidad(), recortar(l.getProducto(), 26),
                    Moneda.pesos(l.getPrecioUnitario()), Moneda.pesos(l.getSubtotal())));

            pedidos.add(l.getIdPedido());
            unidades += l.getCantidad();
            total = total.add(l.getSubtotal());
        }

        sb.append('\n').append(LINEA_SIMPLE).append('\n');
        sb.append("RESUMEN DEL DÍA\n");
        sb.append("Pedidos:            ").append(pedidos.size()).append('\n');
        sb.append("Unidades vendidas:  ").append(unidades).append('\n');
        sb.append("Total vendido:      ").append(Moneda.pesos(total)).append('\n');
        sb.append("(Valores antes de los descuentos aplicados al pedido.)\n");
        return sb.toString();
    }

    private static String recortar(String texto, int max) {
        return texto.length() <= max ? texto : texto.substring(0, max - 1) + "…";
    }
}
