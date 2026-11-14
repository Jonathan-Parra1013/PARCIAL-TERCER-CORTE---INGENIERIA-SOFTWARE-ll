package jonathan.parra1013.tiendaonlineparcial.BusinessLogic;

import java.util.regex.Pattern;


public class EmailValidator {

    /**
     * The class EmailValidator was made to handle the validation of the formats for the email
     * this way, the logic can be reutilized with classes that need it, and the logic is abstracted.
     * */
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
