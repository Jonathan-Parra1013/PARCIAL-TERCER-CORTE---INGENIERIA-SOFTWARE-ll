package jonathan.parra1013.tiendaonlineparcial.Repositories;

import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.Roles;
import jonathan.parra1013.tiendaonlineparcial.BusinessLogic.User;

import java.util.HashMap;
import java.util.Map;


public class UsersRepository implements  IUsersRepository {
    Map<String, User> temp_users = new HashMap<String, User>();
    public UsersRepository() {
        temp_users.put("seller", new User.Builder("seller", "123456", "seller@gmail.com")
                .role(Roles.Seller).build());
    }

    @Override
    public boolean userExists(String username) {
        return temp_users.containsKey(username);
    }
    public boolean emailExists(String email) {
        return temp_users.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }
    @Override
    public void saveUser(User user) {
        temp_users.put(user.getUsername(), user);
    }
}
