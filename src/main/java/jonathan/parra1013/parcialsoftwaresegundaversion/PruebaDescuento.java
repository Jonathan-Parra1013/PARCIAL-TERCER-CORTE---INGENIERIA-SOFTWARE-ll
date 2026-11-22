package jonathan.parra1013.parcialsoftwaresegundaversion;
import LogicaNegocio.Descuentos.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
public class PruebaDescuento {

        public static void prueba() {
            CalculadoraDescuentos calculadora = new CalculadoraDescuentos(
                    List.of(
                            new DescuentoPorVolumen(),
                            new DescuentoClienteFrecuente(),
                            new DescuentoPorMonto(),
                            new DescuentoPorCupon(Map.of("BIENVENIDO10", new BigDecimal("10")))
                    ),
                    new BigDecimal("25")); // tope máximo: 25%

            mostrar("Compra grande con cupón",
                    calculadora.calcular(new Compra(new BigDecimal("800000"), 10, 4, "bienvenido10")));

            mostrar("Compra pequeña",
                    calculadora.calcular(new Compra(new BigDecimal("40000"), 1, 0, null)));
        }

        private static void mostrar(String titulo, ResultadoDescuento r) {
            System.out.println("== " + titulo + " ==");
            System.out.println("Subtotal: " + r.getSubtotal());
            r.getDetalle().forEach(d -> System.out.println("  - " + d));
            System.out.println("Descuento (" + r.getPorcentajeAplicado() + "%): " + r.getMontoDescuento());
            System.out.println("Total: " + r.getTotal());
            System.out.println();

    }
}
