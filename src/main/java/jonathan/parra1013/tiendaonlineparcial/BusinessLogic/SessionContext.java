package jonathan.parra1013.tiendaonlineparcial.BusinessLogic;

public class SessionContext {
    private User currentUser;
    public SessionContext() {
        this.currentUser = null;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logOut(){
        currentUser = null;
    }
}
