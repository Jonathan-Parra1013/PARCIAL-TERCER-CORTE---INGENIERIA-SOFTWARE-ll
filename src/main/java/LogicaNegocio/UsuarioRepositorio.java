package LogicaNegocio;


public interface UsuarioRepositorio {

    boolean existeCorreo(String correo);


    boolean registrarUsuario(Usuario usuario);


    Usuario iniciarSesion(String correo, String contrasenaPlana);
}
