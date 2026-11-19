package LogicaNegocio.Descuentos;

import java.math.BigDecimal;

public interface ReglaDescuento {
    String nombre();

    BigDecimal porcentaje(Compra compra);
}
