package LogicaNegocio;

public class Usuario {
    private final int id;
    private final String nombre;
    private final String email;
    private final String passwordHash;
    private final String rol;

    public Usuario(int id, String nombre, String email, String passwordHash, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRol() { return rol; }

    @Override
    public String toString() {
        return rol + " - " + nombre + " <" + email + ">";
    }
}
