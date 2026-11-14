package jonathan.parra1013.tiendaonlineparcial.BusinessLogic;

import jonathan.parra1013.tiendaonlineparcial.Repositories.IUsersRepository;

import java.util.regex.Pattern;

public class AuthService {
    IUsersRepository userRepo;

    /*Constructor to add the necessary dependencies:
    * iusersRepository: dependency to store the data into the database*/
    public AuthService(IUsersRepository iusersRepository) {
        this.userRepo = iusersRepository;

    }

    //Function to registerUsers into the data base
    public String registerUser(User user) {

        //validating the empty fields
        if(user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            return "One of the fields is empty";
        }

        //validating spaces
        if(user.getUsername().isBlank() || user.getEmail().isBlank() || user.getPassword().isBlank()) {
            return "One of the fields is empty";
        }
        //validating the email format
        if(!EmailValidator.isValid(user.getEmail())) {
            return "Email address is invalid";
        }

        String username = user.getUsername();
        String email = user.getEmail();



        //validating if the username or the email already exists in the data base.
        if(userRepo.userExists(username)) {
            return "Username already exists";
        }
        if(userRepo.emailExists(email)) {
            return "Email already exists";
        }
        userRepo.saveUser(user);
        return "User registered successfully";
    }
}
