package LogicaNegocio.Producto;


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

    // Constructor privado: obliga a crear instancias mediante el Builder
    private Producto(Builder builder) {
        if (builder.nombre == null || builder.nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (builder.nombre.trim().length() > 120) {
            throw new IllegalArgumentException("El nombre es demasiado largo (máximo 120 caracteres).");
        }
        if (builder.precio == null || builder.precio.signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        if (builder.stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (builder.categoria == null || builder.categoria.isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar una categoría.");
        }

        this.id = builder.id;
        this.nombre = builder.nombre.trim();
        this.descripcion = builder.descripcion;
        this.precio = builder.precio;
        this.stock = builder.stock;
        this.categoria = builder.categoria.trim();
        this.idVendedor = builder.idVendedor;
        this.rutaImagen = builder.rutaImagen;
        this.activo = builder.activo;
    }

    public void validarParaPublicar() {
        if (idVendedor <= 0) {
            throw new IllegalArgumentException("El producto debe tener un vendedor.");
        }
        if (rutaImagen == null || rutaImagen.isBlank()) {
            throw new IllegalArgumentException("La imagen del producto es obligatoria.");
        }
    }

    // Devuelve un Builder precargado con los datos actuales para clonar y modificar
    public Builder toBuilder() {
        return new Builder(this.id, this.nombre, this.precio, this.stock)
                .descripcion(this.descripcion)
                .categoria(this.categoria)
                .idVendedor(this.idVendedor)
                .rutaImagen(this.rutaImagen)
                .activo(this.activo);
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getCategoria() { return categoria; }
    public int getIdVendedor() { return idVendedor; }
    public String getRutaImagen() { return rutaImagen; }
    public boolean isActivo() { return activo; }

    // Clase interna estática Builder
    public static class Builder {
        // Campos obligatorios
        private int id;
        private String nombre;
        private BigDecimal precio;
        private int stock;

        // Campos opcionales con valores predeterminados
        private String descripcion = null;
        private String categoria = "General";
        private int idVendedor = 0;
        private String rutaImagen = null;
        private boolean activo = true;

        // Constructor con los datos obligatorios
        public Builder(int id, String nombre, BigDecimal precio, int stock) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }

        // Métodos opcionales (también permiten editar los obligatorios en toBuilder)
        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder precio(BigDecimal precio) {
            this.precio = precio;
            return this;
        }

        public Builder stock(int stock) {
            this.stock = stock;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder categoria(String categoria) {
            this.categoria = categoria;
            return this;
        }

        public Builder idVendedor(int idVendedor) {
            this.idVendedor = idVendedor;
            return this;
        }

        public Builder rutaImagen(String rutaImagen) {
            this.rutaImagen = rutaImagen;
            return this;
        }

        public Builder activo(boolean activo) {
            this.activo = activo;
            return this;
        }

        public Producto build() {
            return new Producto(this);
        }
    }
}
