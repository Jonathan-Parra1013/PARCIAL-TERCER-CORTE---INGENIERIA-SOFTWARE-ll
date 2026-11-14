/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package jonathan.parra1013.tiendaonlineparcial;

import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.AuthService;
import jonathan.parra1013.tiendaonlineparcial.GUI.RegisterGUI;
import jonathan.parra1013.tiendaonlineparcial.Repositories.IUsersRepository;
import jonathan.parra1013.tiendaonlineparcial.Repositories.UsersRepository;

/**
 *
 * @author jonap
 */
public class TiendaOnlineParcial {
    AuthService authService;
    IUsersRepository usersRepository;
    RegisterGUI  registerGUI;
    public static void main(String[] args) {
        TiendaOnlineParcial tienda = new TiendaOnlineParcial();
        tienda.implementRegisterLayers();

        tienda.registerGUI.register();

    }

    private void implementRegisterLayers(){
        usersRepository = new UsersRepository();
        authService = new AuthService(usersRepository);
        registerGUI = new RegisterGUI(authService);
    }
}
