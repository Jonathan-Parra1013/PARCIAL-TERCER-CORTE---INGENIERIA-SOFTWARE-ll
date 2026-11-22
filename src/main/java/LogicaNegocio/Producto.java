package LogicaNegocio;


/**
 *
 * @author jonap
 */


import java.math.BigDecimal;


public class Producto {
    private final int id;
    private final String nombre;
    private final String descripcion;
    private final BigDecimal precio;
    private final int stock;
    private final String categoria;
    private final int idVendedor;
    private final String rutaImagen;
    private final boolean activo;

    public Producto(int id, String nombre, BigDecimal precio, int stock, String categoria) {
        this(id, nombre, null, precio, stock, categoria, 0, null, true);
    }

    public Producto(int id, String nombre, String descripcion, BigDecimal precio, int stock,
                    String categoria, int idVendedor, String rutaImagen, boolean activo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (nombre.trim().length() > 120) {
            throw new IllegalArgumentException("El nombre es demasiado largo (máximo 120 caracteres).");
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
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria.trim();
        this.idVendedor = idVendedor;
        this.rutaImagen = rutaImagen;
        this.activo = activo;
    }

    public void validarParaPublicar() {
        if (idVendedor <= 0) {
            throw new IllegalArgumentException("El producto debe tener un vendedor.");
        }
        if (rutaImagen == null || rutaImagen.isBlank()) {
            throw new IllegalArgumentException("La imagen del producto es obligatoria.");
        }
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getCategoria() { return categoria; }
    public int getIdVendedor() { return idVendedor; }
    public String getRutaImagen() { return rutaImagen; }
    public boolean isActivo() { return activo; }
}
