package jonathan.parra1013.tiendaonlineparcial.Repositories;

import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.User;

public interface IUsersRepository {
    boolean userExists(String username);
    boolean emailExists(String email);
    void saveUser(User user);
}
