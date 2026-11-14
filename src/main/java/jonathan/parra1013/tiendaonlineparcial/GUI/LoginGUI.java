package jonathan.parra1013.tiendaonlineparcial.GUI;

import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.AuthService;
import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.User;

import java.util.Scanner;

public class LoginGUI {
    AuthService auth;
    public LoginGUI(AuthService auth) {
        this.auth = auth;
    }

    public void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Write the username:");
        String username = sc.nextLine();
        System.out.println("Write the password:");
        String password = sc.nextLine();
        User user = new User.Builder(username, password).build();
        System.out.println(auth.loginUser(user));
    }
}
