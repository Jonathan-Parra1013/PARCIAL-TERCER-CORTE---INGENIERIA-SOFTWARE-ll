package jonathan.parra1013.tiendaonlineparcial.GUI;

import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.AuthService;
import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.Roles;
import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.User;

import java.util.Scanner;

public class RegisterGUI {
    AuthService auth;
    public RegisterGUI(AuthService auth) {
        this.auth = auth;
    }
    public void register(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Add username: ");
        String username = sc.nextLine();
        System.out.println("Add password: ");
        String password = sc.nextLine();
        System.out.println("Add email: ");
        String email = sc.nextLine();

        String state = auth.registerUser(new User.Builder(username, password, email)
                .role(Roles.Customer)
                .build());
        
        System.out.println(state);
    }
}
