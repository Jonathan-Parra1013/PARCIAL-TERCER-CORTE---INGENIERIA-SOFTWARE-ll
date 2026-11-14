package jonathan.parra1013.tiendaonlineparcial.BusinessLogic;

public class User {
    private String username;
    private String password;
    private String email;
    private Roles role;
    private User(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.role = builder.role;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() { return email; }
    public Roles getRole() { return role; }

    /*
    * Builder pattern was added because the User might grow of parameters, and fixing the code
    * and the readability would be harder.
    * */
    public static class Builder{
        private String username;
        private String password;
        private String email;
        private Roles role;
        public Builder(String username, String password) {
            this.username = username;
            this.password = password;

        }
        public Builder email(String email){
            this.email = email;
            return this;
        }
        public Builder role(Roles role){
            this.role = role;
            return this;
        }
        public User build(){
            return new User(this);
        }
    }
}
