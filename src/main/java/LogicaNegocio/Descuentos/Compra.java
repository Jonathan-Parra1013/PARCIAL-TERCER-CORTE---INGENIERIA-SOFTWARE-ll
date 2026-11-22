package LogicaNegocio.Descuentos;
import java.math.BigDecimal;

public class Compra {
    private final BigDecimal subtotal;
    private final int cantidadItems;
    private final int comprasPrevias;
    private final String cupon; // puede ser null

    public Compra(BigDecimal subtotal, int cantidadItems, int comprasPrevias, String cupon) {
        this.subtotal = subtotal;
        this.cantidadItems = cantidadItems;
        this.comprasPrevias = comprasPrevias;
        this.cupon = cupon;
    }

    public BigDecimal getSubtotal() { return subtotal; }
    public int getCantidadItems() { return cantidadItems; }
    public int getComprasPrevias() { return comprasPrevias; }
    public String getCupon() { return cupon; }
}
