package jonathan.parra1013.tiendaonlineparcial.BusinessLogic;

import jonathan.parra1013.tiendaonlineparcial.Repositories.IUsersRepository;
import org.mindrot.jbcrypt.BCrypt;


public class AuthService {
    IUsersRepository userRepo;

    /*Constructor to add the necessary dependencies:
    * iusersRepository: dependency to store the data into the database*/
    public AuthService(IUsersRepository iusersRepository) {
        this.userRepo = iusersRepository;

    }

    //Function to register Users into the data base
    public String registerUser(User user) {
        //validating spaces and empty spaces
        if(user.getUsername().isBlank() || user.getEmail().isBlank() || user.getPassword().isBlank()) {
            return "One of the fields is empty";
        }
        //validating the email format
        if(!EmailValidator.isValid(user.getEmail())) {
            return "Email address is invalid";
        }

        String username = user.getUsername();
        String email = user.getEmail();
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        //validating if the username or the email already exists in the data base.
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

    //Login method. Accepts user
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
