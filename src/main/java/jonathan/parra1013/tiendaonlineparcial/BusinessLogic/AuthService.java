package jonathan.parra1013.tiendaonlineparcial.BusinessLogic;

import jonathan.parra1013.tiendaonlineparcial.Repositories.IUsersRepository;
import org.mindrot.jbcrypt.BCrypt;


public class AuthService {

    IUsersRepository userRepo;
    public AuthService(IUsersRepository iusersRepository) {
        this.userRepo = iusersRepository;

    }
    /**
     * registerUser is a function that is used to add new users. It validates the data firstly
     * before using it to be sure it is handling correct data.
     * BCrypt was used to ensure a better security considering the passwords.
     * @param User
     * @return A string text indicating the state of the operation.
     * */
    public String registerUser(User user) {
        if(user.getUsername().isBlank() || user.getEmail().isBlank() || user.getPassword().isBlank()) {
            return "One of the fields is empty";
        }
        if(!EmailValidator.isValid(user.getEmail())) {
            return "Email address is invalid";
        }

        String username = user.getUsername();
        String email = user.getEmail();
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        if(userRepo.userExists(username)) {
            return "Username already exists";
        }
        if(userRepo.emailExists(email)) {
            return "Email already exists";
        }
        userRepo.saveUser(new User.Builder(username, hashedPassword)
                .email(email)
                .role(Roles.Customer)
                .build());

        return "User registered successfully";
    }
    /**
     * loginUser is a function that used to validate credentials and log in users.
     * BCrypt was used to automatically validate hashed passwords.
     * @param User
     * @return A string text indicating the state of the operation.
     * */
    public String loginUser(User user) {
        if(!userRepo.userExists(user.getUsername())){
            return "user doesn't exist";
        }
        if (!BCrypt.checkpw(user.getPassword(), userRepo.getUser(user.getUsername()).getPassword())) {
            return "Wrong password";
        }
        return "User logged successfully";
    }
}
