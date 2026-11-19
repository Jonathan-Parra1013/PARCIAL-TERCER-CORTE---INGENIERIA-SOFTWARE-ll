package LogicaNegocio;

public class Usuario {
    private final String nombre;
    private final String email;
    private final String passwordHash;
    private final Rol rol;

    public Usuario(String nombre, String email, String passwordHash, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Rol getRol() { return rol; }

    @Override
    public String toString() {
        return rol + " - " + nombre + " <" + email + ">";
    }
}
