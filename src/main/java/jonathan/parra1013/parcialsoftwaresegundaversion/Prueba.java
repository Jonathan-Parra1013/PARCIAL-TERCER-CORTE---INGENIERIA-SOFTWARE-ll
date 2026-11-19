package jonathan.parra1013.parcialsoftwaresegundaversion;

import LogicaNegocio.AuthService;
import LogicaNegocio.Rol;
import LogicaNegocio.Usuario;

import java.util.Optional;

public class Prueba {
    public static void iniciar() {
        AuthService auth = new AuthService();

        auth.registrar("Ana Gómez", "ana@correo.com", "clave12345", Rol.CLIENTE);
        Usuario v = auth.registrar("Libros SAS", "ventas@libros.com", "otraClave99", Rol.VENDEDOR);
        System.out.println("Hash guardado: " + v.getPasswordHash());

        Optional<Usuario> ok = auth.autenticar("ana@correo.com", "clave12345");
        System.out.println("Login correcto: " + ok.isPresent() + " -> " + ok.orElse(null));

        Optional<Usuario> mal = auth.autenticar("ana@correo.com", "incorrecta");
        System.out.println("Login con clave incorrecta: " + mal.isPresent());

        try {
            auth.registrar("Otra Ana", "ANA@correo.com", "clave12345", Rol.CLIENTE);
        } catch (IllegalArgumentException e) {
            System.out.println("Registro duplicado: " + e.getMessage());
        }
    }
}
