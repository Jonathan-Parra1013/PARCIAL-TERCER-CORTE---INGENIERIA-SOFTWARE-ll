package LogicaNegocio.Producto;

import LogicaNegocio.Usuario;
import LogicaNegocio.UsuarioService;
import Repositorios.ProductoRepositorio;
import java.math.BigDecimal;
import java.util.List;

public class ProductoService {
    private final ProductoRepositorio repositorio;

    public ProductoService(ProductoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Producto> catalogo(String categoria) {
        return repositorio.filtrar(categoria, null, null, null);
    }

    public List<String> categorias() {
        return repositorio.listarCategorias();
    }

    public List<Producto> productosDe(Usuario vendedor) {
        validarVendedor(vendedor);
        return repositorio.listarPorVendedor(vendedor.getId());
    }

    public void publicar(Usuario vendedor, String nombre, String descripcion, String categoria,
                         BigDecimal precio, int stock, String rutaImagen) {
        validarVendedor(vendedor);
        Producto nuevo = construir(new Producto.Builder(0, nombre, precio, stock), vendedor,
                descripcion, categoria, rutaImagen);
        if (!repositorio.crear(nuevo)) {
            throw new ProductoException("No se pudo guardar el producto. Revisa la conexión con la base de datos.");
        }
    }

    public void modificar(Usuario vendedor, Producto original, String nombre, String descripcion,
                          String categoria, BigDecimal precio, int stock, String rutaImagen) {
        validarVendedor(vendedor);
        if (original == null || original.getIdVendedor() != vendedor.getId()) {
            throw new ProductoException("Solo puedes modificar tus propios productos.");
        }
        Producto cambiado = construir(new Producto.Builder(original.getId(), nombre, precio, stock),
                vendedor, descripcion, categoria, rutaImagen);
        if (!repositorio.actualizar(cambiado)) {
            throw new ProductoException("No se pudo actualizar el producto.");
        }
    }


    public void eliminar(Usuario vendedor, Producto producto) {
        validarVendedor(vendedor);
        if (producto == null || producto.getIdVendedor() != vendedor.getId()) {
            throw new ProductoException("Solo puedes eliminar tus propios productos.");
        }
        if (!repositorio.desactivar(producto.getId(), vendedor.getId())) {
            throw new ProductoException("No se pudo eliminar el producto.");
        }
    }

    private Producto construir(Producto.Builder b, Usuario vendedor, String descripcion,
                               String categoria, String rutaImagen) {
        try {
            Producto p = b.descripcion(descripcion)
                    .categoria(categoria)
                    .idVendedor(vendedor.getId())
                    .rutaImagen(rutaImagen)
                    .build();
            p.validarParaPublicar();
            return p;
        } catch (IllegalArgumentException e) {
            throw new ProductoException(e.getMessage());
        }
    }

    private void validarVendedor(Usuario usuario) {
        if (usuario == null || !UsuarioService.ROL_VENDEDOR.equalsIgnoreCase(usuario.getRol())) {
            throw new ProductoException("Solo los vendedores pueden administrar productos.");
        }
    }
}
