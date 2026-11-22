package LogicaNegocio;

import java.math.BigDecimal;

/**
 * Producto de la tienda. Valida las reglas de RF-04: nombre obligatorio,
 * precio mayor a cero y stock no negativo. Para un producto nuevo (aún sin
 * guardar) se usa id = 0; la base de datos asigna el id real.
 */
public class Producto {
    private final int id;
    private final String nombre;
    private final BigDecimal precio;
    private final int stock;
    private final String categoria;

    public Producto(int id, String nombre, BigDecimal precio, int stock, String categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (precio == null || precio.signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar una categoría.");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria.trim();
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getCategoria() { return categoria; }
}
