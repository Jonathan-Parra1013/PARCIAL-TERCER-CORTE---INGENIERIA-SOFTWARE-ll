package jonathan.parra1013.tiendaonlineparcial.Repositories;

import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.User;

public interface IUsersRepository {
    boolean userExists(String username);
    boolean emailExists(String email);
    User getUser(String username);
    void saveUser(User user);
}
