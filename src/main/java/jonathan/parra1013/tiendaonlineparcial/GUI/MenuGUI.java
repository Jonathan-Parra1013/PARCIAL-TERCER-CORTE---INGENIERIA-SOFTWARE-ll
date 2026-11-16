package jonathan.parra1013.tiendaonlineparcial.GUI;

import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.Roles;
import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.SessionContext;

import java.util.Scanner;

public class MenuGUI {
    //Injecting the different GUIS, at this moments, we only have the login GUI and register GUi
    LoginGUI loginGUI;
    RegisterGUI registerGUI;
    SessionContext sessionContext;
    public MenuGUI(LoginGUI loginGUI, RegisterGUI registerGUI, SessionContext sessionContext) {
        this.loginGUI = loginGUI;
        this.registerGUI = registerGUI;
        this.sessionContext = sessionContext;
    }
    
    public void run(){
        while (true){
            int choice = 0;
            System.out.println("1. Register");
            System.out.println("2. Log in");
            System.out.println("3. Exit");
            Scanner sc = new Scanner(System.in);
            if(sessionContext.getCurrentUser()==null){
                try{
                    choice = Integer.parseInt(sc.nextLine());
                    switch (choice){
                        case 1:
                            registerGUI.register();
                            break;
                        case 2:
                            loginGUI.login();
                            break;
                        case 3:
                            System.exit(0);
                    }


                }catch (NumberFormatException e){
                    System.out.println("The choice must be a number.");
                }
            } else if (sessionContext.getCurrentUser().getRole()== Roles.Seller) {


            }
        }
    }
}
