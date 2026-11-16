/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package jonathan.parra1013.tiendaonlineparcial;

import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.AuthService;
import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.SessionContext;
import jonathan.parra1013.tiendaonlineparcial.GUI.LoginGUI;
import jonathan.parra1013.tiendaonlineparcial.GUI.MenuGUI;
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
    SessionContext sessionContext;
    RegisterGUI  registerGUI;
    LoginGUI loginGUI;
    MenuGUI menuGUI;

    public static void main(String[] args) {
        TiendaOnlineParcial tienda = new TiendaOnlineParcial();
        tienda.implementLayers();

        tienda.menuGUI.run();
    }

    private void implementLayers(){
        usersRepository = new UsersRepository();
        authService = new AuthService(usersRepository, sessionContext);
        registerGUI = new RegisterGUI(authService);
        loginGUI = new LoginGUI(authService);
        menuGUI = new MenuGUI(loginGUI, registerGUI, sessionContext);
    }
}
