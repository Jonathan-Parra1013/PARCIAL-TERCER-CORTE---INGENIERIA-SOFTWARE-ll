package LogicaNegocio;

public class UsuarioService {
    // Deben coincidir con los valores del enum usuarios.rol en la base de datos
    public static final String ROL_CLIENTE = "CLIENTE";
    public static final String ROL_VENDEDOR = "VENDEDOR";

    private final UsuarioRepositorio repositorio;

    public UsuarioService(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Usuario iniciarSesion(String correo, String clave) {
        String correoLimpio = correo == null ? "" : correo.trim().toLowerCase();
        if (correoLimpio.isEmpty() || clave == null || clave.isEmpty()) {
            throw new UsuarioException(null, "Ingresa tu correo y tu contraseña.");
        }
        Usuario usuario = repositorio.iniciarSesion(correoLimpio, clave);
        if (usuario == null) {
            throw new UsuarioException(null, "Correo o contraseña incorrectos.");
        }
        return usuario;
    }


    public void registrar(String nombre, String correo, String clave, String confirmacion, String rol) {
        String nombreLimpio = nombre == null ? "" : nombre.trim();
        String correoLimpio = correo == null ? "" : correo.trim().toLowerCase();

        if (nombreLimpio.isEmpty()) {
            throw new UsuarioException("nombre", "El nombre es obligatorio.");
        }
        if (nombreLimpio.length() > 100) {
            throw new UsuarioException("nombre", "El nombre es demasiado largo (máximo 100 caracteres).");
        }
        if (!correoLimpio.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || correoLimpio.length() > 120) {
            throw new UsuarioException("correo", "Ingresa un correo válido.");
        }
        if (clave == null || clave.length() < 8) {
            throw new UsuarioException("clave", "La contraseña debe tener al menos 8 caracteres.");
        }
        if (!clave.equals(confirmacion)) {
            throw new UsuarioException("confirmacion", "Las contraseñas no coinciden.");
        }
        if (!ROL_CLIENTE.equals(rol) && !ROL_VENDEDOR.equals(rol)) {
            throw new UsuarioException("rol", "Selecciona un tipo de cuenta válido.");
        }
        if (repositorio.existeCorreo(correoLimpio)) {
            throw new UsuarioException("correo", "Ya existe una cuenta con ese correo.");
        }
        // el DAO cifra la contraseña con BCrypt antes de guardarla
        if (!repositorio.registrarUsuario(new Usuario(0, nombreLimpio, correoLimpio, clave, rol))) {
            throw new UsuarioException(null, "No se pudo crear la cuenta. Revisa la conexión con la base de datos.");
        }
    }
}
