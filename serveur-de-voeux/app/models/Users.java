package models;

import com.avaje.ebean.Ebean;
//import controllers.admin.UsersController;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
public class Users extends Model {

    public enum Role {
        ADMIN,
        USER;
    }

    @Id public String login;
    @Constraints.Email public String email;

    @Column(nullable = false) public String password;
    @Column(nullable = false) public @Enumerated(EnumType.STRING) Role role;
    public String firstName;
    public String lastName;
    public String address;

    @Constraints.Pattern(value = "[0-9.+]+", message = "A valid phone number is required")
    public String phoneNumber;

    public Users()
    {
        this.login = "";
        this.email = "";
        this.password ="";
        this.role = Role.ADMIN;
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.phoneNumber = "0606060606";
    }

    public Users(String login, String email, String password, Role role, String firstName, String lastName, String address, String phoneNumber) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users)) return false;
        if (!super.equals(o)) return false;

        Users user = (Users) o;

        if (address != null ? !address.equals(user.address) : user.address != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (!login.equals(user.login)) return false;
        if (!password.equals(user.password)) return false;
        if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null) return false;
        if (role != user.role) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + password.hashCode();
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

    public static Finder<String, Users> find = new Finder<String, Users>(
            String.class, Users.class
    );

    /**
     * Retourne la liste des utilisateurs
     * @return
     */
    public static List<Users> show(){
        return find.orderBy("LAST_NAME ASC").findList();
    }

    public static Users create(String login, String email, String password, Role role, String firstName, String lastName, String address, String phoneNumber) {
        Users user = new Users(login,email,password,role,firstName,lastName,address,phoneNumber);
        Ebean.save(user);
        return user;
    }

    /**
     * Mise à jours du profil de l'utilisateur
     * @param login
     * @param email
     * @param password
     * @param role
     * @param firstName
     * @param lastName
     * @param address
     * @param phoneNumber
     * @return
     */
    public static Users update(String login, String email, String password, String role, String firstName, String lastName, String address, String phoneNumber){
        Users user = find.where()
                         .eq("login",login)
                         .findUnique();

        user.setRole(role(role));
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.save();

        return user;
    }

    /**
     * Authentification
     * @param login
     * @param password
     * @return
     */
    public static Users authenticate(String login,String password){
        return find.where()
                .eq("login",login)
                .eq("password",password)
                .findUnique();
    }

    public String role(){
        if(this.role == Role.USER)
            return "USER";
        return "ADMIN";
    }

    public static Role role(String roleu){
        if(roleu.equals("USER")) {
            return Role.USER;
        }
        return Role.ADMIN;
    }

    /**
     * Trouver un utilisateur par son identifiant
     * @param iduser
     * @return
     */
    public static Users findUsersById(String iduser)
    {
        return find.byId(iduser);
    }

    /**
     * Supprimer un utilisateur
     * @param login
     */
    public static void deleteUser(String login)
    {
        Users user = find.where().eq("LOGIN", login).findUnique();

        Ebean.delete(user);
    }

    public static String getUserName(String login){
        Users t = find.where()
                .eq("login", login)
                .findUnique();
        return  (t.getFirstName() + " " + t.getLastName());
    }
}
