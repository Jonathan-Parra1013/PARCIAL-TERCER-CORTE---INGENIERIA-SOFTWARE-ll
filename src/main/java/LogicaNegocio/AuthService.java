package LogicaNegocio;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthService {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    public Usuario registrar(String nombre, String email, String password, Rol rol) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }

        String clave = email.trim().toLowerCase();
        if (usuarios.containsKey(clave)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        String hash = PasswordHasher.hash(password.toCharArray());
        Usuario usuario = new Usuario(nombre.trim(), clave, hash, rol);
        usuarios.put(clave, usuario);
        return usuario;
    }

    /** Devuelve el usuario si las credenciales son correctas; vacío si no. */
    public Optional<Usuario> autenticar(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }
        Usuario usuario = usuarios.get(email.trim().toLowerCase());
        if (usuario == null) {
            return Optional.empty();
        }
        boolean ok = PasswordHasher.verificar(password.toCharArray(), usuario.getPasswordHash());
        return ok ? Optional.of(usuario) : Optional.empty();
    }
}
