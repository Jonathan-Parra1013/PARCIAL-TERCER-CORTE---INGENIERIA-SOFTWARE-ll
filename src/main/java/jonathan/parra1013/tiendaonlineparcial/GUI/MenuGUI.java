package jonathan.parra1013.tiendaonlineparcial.GUI;

import java.util.Scanner;

public class MenuGUI {
    LoginGUI loginGUI;
    RegisterGUI registerGUI;
    public MenuGUI(LoginGUI loginGUI, RegisterGUI registerGUI) {
        this.loginGUI = loginGUI;
        this.registerGUI = registerGUI;
    }

    public void run(){
        while (true){
            int choice = 0;
            System.out.println("1. Register");
            System.out.println("2. Log in");
            System.out.println("3. Exit");
            Scanner sc = new Scanner(System.in);
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
        }
    }
}
